package edu.cmu.minecraft.betrayal.worldgen.furniture;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Button;
import org.bukkit.material.Wool;

public class Television implements Furniture {

	private int lowX;
	private int baseY;
	private int lowZ;
	private int tvWidth;
	private int tvHeight;
	private BlockFace facing;
	private boolean isNS; // is North-South
	private ArrayList<Location> locs;
	
	public Television(int lowX, int baseY, int lowZ, int tvWidth, int tvHeight, BlockFace facing) {
		this.lowX = lowX;
		this.baseY = baseY;
		this.lowZ = lowZ;
		this.tvWidth = tvWidth;
		this.tvHeight = tvHeight;
		this.facing = facing;
		this.isNS = facing.equals(BlockFace.EAST) || facing.equals(BlockFace.WEST);
		locs = new ArrayList<Location>();
	}
	
	@Override
	public void materialize(World w) {
		if (isNS) {
			for (int z = lowZ + 1; z <= lowZ + tvWidth; z++) {
				w.getBlockAt(lowX, baseY + 1, z).setType(Material.SMOOTH_BRICK);
				locs.add(new Location(w, lowX, baseY+1, z));
			}
			w.getBlockAt(lowX, baseY + 1, lowZ).setType(Material.BOOKSHELF);
			locs.add(new Location(w, lowX, baseY+1, lowZ));
			w.getBlockAt(lowX, baseY + 1, lowZ + tvWidth + 1).setType(Material.BOOKSHELF);
			locs.add(new Location(w, lowX, baseY+1, lowZ + tvWidth + 1));
			for (int y = baseY+2; y <= baseY + 1 + tvHeight; y++) {
				w.getBlockAt(lowX, y, lowZ).setType(Material.BOOKSHELF);
				locs.add(new Location(w, lowX, y, lowZ));
				w.getBlockAt(lowX, y, lowZ+tvWidth+1).setType(Material.BOOKSHELF);
				locs.add(new Location(w, lowX, y, lowZ+tvWidth+1));
				for (int z = lowX + 1; z <= lowX+tvWidth; z++) {
					setWoolBlock(w, lowX, y, z, DyeColor.BLACK);
					locs.add(new Location(w, lowX, y, z));
				}
			}
			if (facing.equals(BlockFace.EAST)) {
				setStoneButton(w, lowX+1, baseY+1, lowZ+1, facing);
				setStoneButton(w, lowX+1, baseY+1, lowZ+tvWidth, facing);
				locs.add(new Location(w, lowX+1, baseY+1, lowZ+1));
				locs.add(new Location(w, lowX+1, baseY+1, lowZ+tvWidth));
			} else if (facing.equals(BlockFace.WEST)) {
				setStoneButton(w, lowX-1, baseY+1, lowZ+1, facing);
				setStoneButton(w, lowX-1, baseY+1, lowZ+tvWidth, facing);
				locs.add(new Location(w, lowX-1, baseY+1, lowZ+1));
				locs.add(new Location(w, lowX-1, baseY+1, lowZ+tvWidth));
			}
		} else {
			for (int x = lowX + 1; x <= lowX + tvWidth; x++) {
				w.getBlockAt(x, baseY + 1, lowZ).setType(Material.SMOOTH_BRICK);
				locs.add(new Location(w, x, baseY+1, lowZ));
			}
			w.getBlockAt(lowX, baseY + 1, lowZ).setType(Material.BOOKSHELF);
			locs.add(new Location(w, lowX, baseY+1, lowZ));
			w.getBlockAt(lowX + tvWidth + 1, baseY + 1, lowZ).setType(Material.BOOKSHELF);
			locs.add(new Location(w, lowX + tvWidth + 1, baseY+1, lowZ));
			for (int y = baseY+2; y <= baseY + 1 + tvHeight; y++) {
				w.getBlockAt(lowX, y, lowZ).setType(Material.BOOKSHELF);
				locs.add(new Location(w, lowX, y, lowZ));
				w.getBlockAt(lowX+tvWidth+1, y, lowZ).setType(Material.BOOKSHELF);
				locs.add(new Location(w, lowX+tvWidth+1, y, lowZ));
				for (int x = lowX + 1; x <= lowX+tvWidth; x++) {
					setWoolBlock(w, x, y, lowZ, DyeColor.BLACK);
					locs.add(new Location(w, x, y, lowZ));
				}
			}
			if (facing.equals(BlockFace.SOUTH)) {
				setStoneButton(w, lowX+1, baseY+1, lowZ+1, facing);
				setStoneButton(w, lowX+tvWidth, baseY+1, lowZ+1, facing);
				locs.add(new Location(w, lowX+1, baseY+1, lowZ+1));
				locs.add(new Location(w, lowX+tvWidth, baseY+1, lowZ+1));
			} else if (facing.equals(BlockFace.NORTH)) {
				setStoneButton(w, lowX+1, baseY+1, lowZ-1, facing);
				setStoneButton(w, lowX+tvWidth, baseY+1, lowZ-1, facing);
				locs.add(new Location(w, lowX+1, baseY+1, lowZ-1));
				locs.add(new Location(w, lowX+tvWidth, baseY+1, lowZ-1));
			}
		}
	}

	private void setWoolBlock(World w, int x, int y, int z, DyeColor color) {
		Wool wool = new Wool(color);
		Block b = w.getBlockAt(x,y,z);
		b.setType(wool.getItemType());
		BlockState state = b.getState();
		state.setData(wool);
		state.update(true);
	}
	
	private void setStoneButton(World w, int x, int y, int z, BlockFace facing) {
		Button b = new Button(Material.STONE_BUTTON);
		Block block = w.getBlockAt(x, y, z);
		block.setType(b.getItemType());
		BlockState state = block.getState();
		b.setFacingDirection(facing);
		state.setData(b);
		state.update(true);
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
