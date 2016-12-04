package edu.cmu.minecraft.betrayal.worldgen.furniture;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Stairs;

public class Sofa implements Furniture {

	private int lowX;
	private int baseY;
	private int lowZ;
	private int width;
	private BlockFace facing;
	private ArrayList<Location> locs;
	private boolean isNS;
	
	public Sofa(int lowX, int baseY, int lowZ, int width, BlockFace facing) {
		this.lowX = lowX;
		this.baseY = baseY;
		this.lowZ = lowZ;
		this.width = width;
		this.facing = facing;
		this.locs = new ArrayList<Location>();
		this.isNS = facing.equals(BlockFace.NORTH) || facing.equals(BlockFace.SOUTH);
	}
	
	@Override
	public void materialize(World w) {
		if (isNS) {
			for (int z = lowZ; z <= lowZ+width; z++) {
				setStair(w, lowX, baseY + 1, z, getStairFacingDir(facing));
				locs.add(new Location(w, lowX, baseY+1, z));
			}
			setSign(w, lowX, baseY+1, lowZ-1, BlockFace.NORTH);
			setSign(w, lowX, baseY+1, lowZ+width+1, BlockFace.SOUTH);
			locs.add(new Location(w, lowX, baseY+1, lowZ-1));
			locs.add(new Location(w, lowX, baseY+1, lowZ+width+1));
		} else {
			for (int x = lowX; x <= lowX+width; x++) {
				setStair(w, x, baseY + 1, lowZ, getStairFacingDir(facing));
				locs.add(new Location(w, x, baseY+1, lowZ));
			}
			setSign(w, lowX-1, baseY+1, lowZ, BlockFace.WEST);
			setSign(w, lowX+width+1, baseY+1, lowZ, BlockFace.EAST);
			locs.add(new Location(w, lowX-1, baseY+1, lowZ));
			locs.add(new Location(w, lowX+width+1, baseY+1, lowZ));
		}
	}

	private void setStair(World w, int x, int y, int z, BlockFace facing) {
		Stairs stair = new Stairs(Material.WOOD_STAIRS);
		Block b = w.getBlockAt(x,y,z);
		b.setType(stair.getItemType());
		BlockState state = b.getState();
		stair.setFacingDirection(facing);
		state.setData(stair);
		state.update(true);
	}
	
	private void setSign(World w, int x, int y, int z, BlockFace facing) {
		org.bukkit.material.Sign s = new org.bukkit.material.Sign(Material.WALL_SIGN);
		Block block = w.getBlockAt(x, y, z);
		block.setType(s.getItemType());
		BlockState state = block.getState();
		s.setFacingDirection(facing);
		state.setData(s);
		state.update(true);
	}
	
	private BlockFace getStairFacingDir(BlockFace facing) {
		switch(facing) {
		case NORTH:
			return BlockFace.WEST;
		case SOUTH:
			return BlockFace.EAST;
		case EAST:
			return BlockFace.NORTH;
		case WEST:
			return BlockFace.SOUTH;
		default:
			return BlockFace.NORTH;
		}
	}
	
	@Override
	public boolean contains(Location l) {
		for (Location loc : locs) {
			if (l.equals(loc)) {
				return true;
			}
		}
		return false;
	}

}
