package edu.cmu.minecraft.betrayal.worldgen;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Door;
import org.bukkit.plugin.Plugin;

import edu.cmu.minecraft.betrayal.entities.Room.InvalidDoorException;

public class Entrance implements Materializable {

	final private World world;
	// Describes location of bottom left door block
	final private Block loc;
	final private Material material;
	final private BlockFace facing;
	final private boolean doubleDoor;
	final private Logger logger;
	private boolean opened;
	
	public Entrance (Plugin p, World w, Material m, BlockFace f, boolean doubleDoor, int x, int y, int z) {
		this.logger = p.getLogger();
		world = w;
		loc = world.getBlockAt(x, y, z);
		material = m;
		this.doubleDoor = doubleDoor;
		this.opened = false;
		this.facing = f;
	}
	
	public Block getLoc() {
		return loc;
	}

	public Material getMaterial() {
		return material;
	}

	public boolean isOpened() {
		return opened;
	}
	
	public void open() {
		this.opened = true;
	}
	
	public boolean isDoubleDoor() {
		return doubleDoor;
	}

	public boolean includesBlock(Block b) {
		if (b.equals(loc)) { return true;}
		if (b.equals(loc.getRelative(BlockFace.UP))) { return true;}
		if (doubleDoor) {
			Block rightDoor;
			try {
				rightDoor = loc.getRelative(computeRightDoorDirection(facing));
				if (b.equals(rightDoor)) { return true;}
				if (b.equals(rightDoor.getRelative(BlockFace.UP))) { return true;}
			} catch (InvalidDoorException e) {
				logger.warning("Invalid door facing direction: " + facing);
			}
		}
		return false;
	}
	
	@Override
	public void materialize() {
		Block rightDoor;
		generateDoor(loc.getX(), loc.getY(), loc.getZ(), facing, false);
		if (doubleDoor) {
			try {
				rightDoor = loc.getRelative(computeRightDoorDirection(facing));
				generateDoor(rightDoor.getX(), rightDoor.getY(), rightDoor.getZ(), facing, true);
			} catch (InvalidDoorException e) {
				logger.warning("Invalid door facing direction: " + facing);
			}
		}
	}
	
	private void generateDoor(int x, int y, int z, BlockFace facing, boolean hingeRight) {
		Block doorBlockBot = world.getBlockAt(x, y, z);
		Block doorBlockTop = world.getBlockAt(x, y + 1, z);
		Door doorBot = new Door(material, facing, false);
		Door doorTop = new Door(material, hingeRight);
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
	
}
