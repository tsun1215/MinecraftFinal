package edu.cmu.minecraft.betrayal.worldgen;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.material.Door;
import org.bukkit.plugin.Plugin;

public class StartHousePopulator extends BlockPopulator {
	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_HEIGHT = 16;
	public static final int CEILING_HEIGHT = 6;
	
	private Logger logger;
	
	public StartHousePopulator(Plugin plugin) {
		super();
		this.logger = plugin.getLogger();
	}
	
	private void generateHouse(World world, Chunk chunk) {
		int cX = chunk.getX() * CHUNK_WIDTH;
		int cZ = chunk.getZ() * CHUNK_HEIGHT;
		// Set floor of house to be highest block
		int y = world.getHighestBlockYAt(cX, cZ) - 1;
		// TODO: This could lead to the house seeming to float if the land is not flat
		//  Instead, we could get the min of all the highest blocks, and shave the earth.
		
		for (int offsetX = 0; offsetX < CHUNK_WIDTH; offsetX++) {
			for (int offsetZ = 0; offsetZ < CHUNK_HEIGHT; offsetZ++) {
				int x = offsetX + cX;
				int z = offsetZ + cZ;
				// Generate floor
				world.getBlockAt(x, y, z).setType(Material.WOOD);
				
				// Generate ceiling
				world.getBlockAt(x, y + CEILING_HEIGHT, z).setType(Material.WOOD);
				
				// Generate walls
				if (offsetX == 0 || offsetX == CHUNK_WIDTH - 1 
						|| offsetZ == 0 || offsetZ == CHUNK_HEIGHT - 1) {
					for (int wallOffet = 0; wallOffet < CEILING_HEIGHT; wallOffet++) {
						world.getBlockAt(x, y + wallOffet, z).setType(Material.WOOD);
					}
				}
			}
		}
		// Generate Door on the EAST side of the house
		generateDoubleDoor(world, cX + CHUNK_WIDTH - 1, y + 1, cZ + (CHUNK_HEIGHT/2 - 1), 
				BlockFace.EAST, false);
	}

	@Override
	public void populate(World world, Random random, Chunk source) {
		int cX = source.getX() * 16;
		int cZ = source.getZ() * 16;
		if (cX > -100 && cX < 100 && cZ > -100 && cZ < 100) {
			if (random.nextInt(100) > 80) {
				generateHouse(world, source);
			}
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
			logger.warning("Invalid door facing direction: " + facing);
		}
	}
	
	private void generateDoor(World world, int x, int y, int z, BlockFace facing, boolean hingeRight, boolean open) {
		Block doorBlockBot = world.getBlockAt(x, y, z);
		Block doorBlockTop = world.getBlockAt(x, y, z);
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
