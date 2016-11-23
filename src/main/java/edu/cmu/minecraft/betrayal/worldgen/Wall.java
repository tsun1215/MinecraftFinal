package edu.cmu.minecraft.betrayal.worldgen;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Directional;

public class Wall implements Materializable, Directional {

	// Corners describe walls inclusively (corners lie within the wall)
	final private Location lowCorner;
	final private Location highCorner; // Corner diagonally opposite lowCorner
	final private Material material;
	private BlockFace facing;

	public Wall(Material m, Location low, Location high, BlockFace facing) {
		this.material = m;
		this.lowCorner = low;
		this.highCorner = high;
	}

	public Location getLowCorner() {
		return lowCorner;
	}

	public Location getHighCorner() {
		return highCorner;
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public void materialize(World w) {
		for (int x = lowCorner.getBlockX(); x <= highCorner.getBlockX(); x++) {
			for (int y = lowCorner.getBlockY(); y <= highCorner.getBlockY(); y++) {
				for (int z = lowCorner.getBlockZ(); z <= highCorner.getBlockZ(); z++) {
					w.getBlockAt(x, y, z).setType(this.material);
				}
			}
		}
	}

	@Override
	public boolean contains(Location l) {
		return lowCorner.getBlockX() < l.getBlockX() && l.getBlockX() < highCorner.getBlockX()
				&& lowCorner.getBlockY() < l.getBlockY() && l.getBlockY() < highCorner.getBlockY()
				&& lowCorner.getBlockZ() < l.getBlockZ() && l.getBlockZ() < highCorner.getBlockZ();
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
