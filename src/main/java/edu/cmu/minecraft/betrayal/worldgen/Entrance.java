package edu.cmu.minecraft.betrayal.worldgen;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Directional;
import org.bukkit.material.Door;
import org.bukkit.plugin.Plugin;

public class Entrance implements Materializable, Directional {

	// Describes location of bottom left door block
	final private Location loc;
	final private Material material;
	private BlockFace facing;
	final private boolean isDoubleDoor;
	final private Logger logger;
	private boolean opened;
	private BlockFace rightDoorDirection;

	public Entrance(Plugin p, Location loc, Material m, BlockFace facing,
			boolean isDoubleDoor) {
		this.logger = p.getLogger();
		this.loc = loc;
		this.material = m;
		this.isDoubleDoor = isDoubleDoor;
		this.opened = false;
		this.facing = facing;
		try {
			this.rightDoorDirection = computeRightDoorDirection(facing);
		} catch (InvalidDoorException e) {
			logger.warning("Invalid door facing direction: " + facing);
		}
	}

	public Location getLoc() {
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
		return isDoubleDoor;
	}

	public boolean includesBlock(Block b) {
		if (b.equals(loc.getBlock())) {
			return true;
		}
		if (b.equals(loc.getBlock().getRelative(BlockFace.UP))) {
			return true;
		}
		if (isDoubleDoor) {
            Block rightDoor = loc.getBlock().getRelative(this.rightDoorDirection);
			if (b.equals(rightDoor)) {
				return true;
			}
			if (b.equals(rightDoor.getRelative(BlockFace.UP))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void materialize(World w) {
		Block rightDoor = loc.getBlock().getRelative(this.rightDoorDirection);
		generateDoor(w, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), facing, false);
		if (isDoubleDoor) {
			generateDoor(w, rightDoor.getX(), rightDoor.getY(),
					rightDoor.getZ(), facing, true);
		}
	}

	private void generateDoor(World w, int x, int y, int z, BlockFace facing,
			boolean hingeRight) {
		Block doorBlockBot = w.getBlockAt(x, y, z);
		Block doorBlockTop = w.getBlockAt(x, y + 1, z);
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

	private BlockFace computeRightDoorDirection(BlockFace doorFace)
			throws InvalidDoorException {
		switch (doorFace) {
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

	public BlockFace getRightDoorDirection() {
		return this.rightDoorDirection;
	}

	// TODO: Extract this class
	public static class InvalidDoorException extends Exception {
		private static final long serialVersionUID = -934628980768245364L;

		public InvalidDoorException(BlockFace b) {
			super(b.toString());
		}
	}

	@Override
	public boolean contains(Location l) {
		// Checks in straight line from door
		switch (this.getFacing()) {
		case NORTH:
		case SOUTH:
			return l.getX() == this.getLoc().getX()
					&& (l.getY() > this.getLoc().getY()
							&& l.getY() < this.getLoc().getY() + 1);
		case EAST:
		case WEST:
			return l.getZ() == this.getLoc().getZ()
					&& (l.getY() > this.getLoc().getY()
							&& l.getY() < this.getLoc().getY() + 1);
		default:
			return l.equals(this.loc);
		}
	}

	@Override
	public void setFacingDirection(BlockFace face) {
		this.facing = face;
	}

	@Override
	public BlockFace getFacing() {
		return this.facing;
	}

}
