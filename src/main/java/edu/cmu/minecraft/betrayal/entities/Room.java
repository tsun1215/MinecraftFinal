package edu.cmu.minecraft.betrayal.entities;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Door;

public class Room {
	
	private World world;
	private int minX, maxX, minY, maxY, minZ, maxZ;
	private Material material;
	private Logger logger;
	
	public Room(World world, int minX, int maxX, int minZ, int maxZ, int height, Material material, Logger logger) {
		this.minX = minX;
		this.maxX = maxX;
		this.minZ = minZ;
		this.maxZ = maxZ;
		
		int highest = -1;
		for (int x = minX+1; x < maxX; x++) {
			for (int z = minZ+1; z < maxZ; z++) {
				if (world.getHighestBlockYAt(x, z) > highest) {
					highest = world.getHighestBlockYAt(x, z);
				}
			}
		}
		this.minY = highest;
		// TODO: This could lead to the house seeming to float if the land is not flat
		//  Instead, we could get the min of all the highest blocks, and shave the earth.
		this.maxY = this.minY + height;
		
		
		this.material = material;
		this.world = world;
		this.logger = logger;
	}
	
	public void generateRoom() {
		logger.info("maxY = " +this.maxY + " minY = " + this.minY);
		logger.info("maxX = " +this.maxX + " minX = " + this.minX);
		logger.info("maxZ = " +this.maxZ + " minZ = " + this.minZ);

		// Generate Floor & Ceiling & Walls
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				
				// Floor 
				this.world.getBlockAt(x, minY, z).setType(this.material);
				// Ceiling
				this.world.getBlockAt(x, maxY, z).setType(this.material);
				
			}
		}
		generateNSWall(world, minX, minY, maxY, minZ, maxZ, material);
		generateNSWall(world, maxX, minY, maxY, minZ, maxZ, material);
		generateEWWall(world, minX, maxX, minY, maxY, minZ, material);
		generateEWWall(world, minX, maxX, minY, maxY, maxZ, material);
	}
	
	// TODO wouldn't be bad if we ignored if there was already a door there
	private void generateNSWall(World world, int X, int minY, int maxY, int minZ, int maxZ, Material material) {
		int longestSharedMin = -1;
		int longestSharedMax = -1;
		int sharedMin = -1;
		int sharedMax = -1;
		boolean inShared = false;
		for (int z = minZ; z <= maxZ; z++) {
			if (world.getBlockAt(X, minY+1, z).getType() != Material.AIR) {
				if (!inShared) {
					sharedMin = z;
				}
				inShared = true;
				sharedMax = z + 1; // Exclusive upper bound
			} else {
				if (sharedMax - sharedMin > longestSharedMax - longestSharedMin) {
					longestSharedMax = sharedMax;
					longestSharedMin = sharedMin;
				}
				inShared = false;
				sharedMax = -1;
				sharedMin = -1;
			}
			for (int y = minY; y <= maxY; y++) {
				world.getBlockAt(X, y, z).setType(material);
			}
		}
		if (sharedMax - sharedMin > longestSharedMax - longestSharedMin) {
			longestSharedMax = sharedMax;
			longestSharedMin = sharedMin;
		}
		logger.info("NS shared = " + longestSharedMax);
		if (longestSharedMax != -1) {
			int doorZ = ((longestSharedMax - longestSharedMin) / 2) + longestSharedMin;
			logger.info("DoorZ = " + doorZ);

			generateDoubleDoor(world, X, minY+1, doorZ, BlockFace.EAST, false);
		}
	}
	
	private void generateEWWall(World world, int minX, int maxX, int minY, int maxY, int Z, Material material) {
		int longestSharedMin = -1;
		int longestSharedMax = -1;
		int sharedMin = -1;
		int sharedMax = -1;
		boolean inShared = false;
		for (int x = minX; x <= maxX; x++) {
			if (world.getBlockAt(x, minY+1, Z).getType() != Material.AIR) {
				if (!inShared) {
					sharedMin = x;
				}
				inShared = true;
				sharedMax = x + 1; // Exclusive upper bound
			} else {
				logger.info("Shared {min,max} = " + sharedMin + " " + sharedMax);
				if (sharedMax - sharedMin > longestSharedMax - longestSharedMin) {
					longestSharedMax = sharedMax;
					longestSharedMin = sharedMin;
				}
				inShared = false;
				sharedMax = -1;
				sharedMin = -1;
			}
			for (int y = minY; y <= maxY; y++) {
				world.getBlockAt(x, y, Z).setType(material);
			}
		}
		if (sharedMax - sharedMin > longestSharedMax - longestSharedMin) {
			longestSharedMax = sharedMax;
			longestSharedMin = sharedMin;
		}
		logger.info("EW shared = " + longestSharedMax);
		if (longestSharedMax != -1) {
			int doorX = ((longestSharedMax - longestSharedMin) / 2) + longestSharedMin;
			logger.info("DoorX = " + doorX);
			generateDoubleDoor(world, doorX, minY+1, Z, BlockFace.NORTH, false);
		}
	}
	
	public static class InvalidWallException extends Exception {
		private static final long serialVersionUID = -955219346295474932L;

		public InvalidWallException(int minX, int maxX, int minZ, int maxZ) {
			super(String.format("minX = %d, maxX = %d, minZ = %d, maxZ = %d", minX, maxX,minZ, maxZ));
		}
	}
	
	private void generateDoubleDoor(World world, int x, int y, int z, BlockFace facing, boolean open) {
		Block leftDoor = world.getBlockAt(x, y, z);
		Block rightDoor;
		try {
			rightDoor = leftDoor.getRelative(computeRightDoorDirection(facing));
			// Left Door
			generateDoor(world, x, y, z, facing, false, open);
			
			// Right Door
			generateDoor(world, rightDoor.getX(), rightDoor.getY(), rightDoor.getZ(), facing, true, open);
		} catch (InvalidDoorException e) {
			//logger.warning("Invalid door facing direction: " + facing);
		}
	}
	
	private void generateDoor(World world, int x, int y, int z, BlockFace facing, boolean hingeRight, boolean open) {
		Block doorBlockBot = world.getBlockAt(x, y, z);
		Block doorBlockTop = world.getBlockAt(x, y + 1, z);
		Door doorBot = new Door(TreeSpecies.DARK_OAK, facing, open);
		Door doorTop = new Door(TreeSpecies.DARK_OAK, hingeRight);
		BlockState state;

		/* Bottom of door */
		doorBlockBot.setType(doorBot.getItemType());
		state = doorBlockBot.getState();
		state.setData(doorBot);
		state.update(true);
		
		/* Top of door */	
		doorBlockTop.setType(doorTop.getItemType());
		state = doorBlockTop.getState();
		state.setData(doorTop);
		state.update(true);
	}
	
	private BlockFace computeRightDoorDirection(BlockFace doorFace) throws InvalidDoorException {
		switch(doorFace) {
		case NORTH:
			return BlockFace.WEST;
		case SOUTH:
			return BlockFace.EAST;
		case EAST:
			return BlockFace.NORTH;
		case WEST:
			return BlockFace.SOUTH;
		default:
			throw new InvalidDoorException(doorFace);
		}
	}
	
	public static class InvalidDoorException extends Exception {
		private static final long serialVersionUID = -934628980768245364L;

		public InvalidDoorException(BlockFace b) {
			super(b.toString());
		}
	}

}
