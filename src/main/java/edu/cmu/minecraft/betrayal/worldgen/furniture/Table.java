package edu.cmu.minecraft.betrayal.worldgen.furniture;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.WoodenStep;

public class Table implements Furniture {

	private int length;
	private int width;
	private int lowX;
	private int baseY;
	private int lowZ;
	
	/**
	 * @param lowX  Lowest X coordinate within the table
	 * @param baseY Y coordinate of floor that table will rest on
	 * @param lowZ  Lowest Z coordinate within the table
	 * @param length Size across the X dimension
	 * @param width Size across the Z dimension
	 */
	public Table(int lowX, int baseY, int lowZ, int length, int width) {
		this.lowX = lowX;
		this.baseY = baseY;
		this.lowZ = lowZ;
		this.width = width;
		this.length = length;
	}
	
	@Override
	public void materialize(World w) {
		w.getBlockAt(lowX, baseY + 1, lowZ).setType(Material.BIRCH_FENCE);
		w.getBlockAt(lowX, baseY + 1, lowZ+width).setType(Material.BIRCH_FENCE);
		w.getBlockAt(lowX+length, baseY + 1, lowZ).setType(Material.BIRCH_FENCE);
		w.getBlockAt(lowX+length, baseY + 1, lowZ+width).setType(Material.BIRCH_FENCE);
		
		for (int x = lowX; x <= lowX+length; x++) {
			for (int z = lowZ; z <= lowZ+width; z++) {
				setStair(w, x, baseY+2,z);
			}
		}
	}

	private void setStair(World w, int x, int y, int z) {
		WoodenStep step = new WoodenStep(TreeSpecies.BIRCH, false);
		Block b = w.getBlockAt(x,y,z);
		b.setType(step.getItemType());
		BlockState state = b.getState();
		state.setData(step);
		state.update(true);
	}
	
	@Override
	public boolean contains(Location l) {
		for (int x = lowX; x <= lowX+length; x++) {
			for (int z = lowZ; z <= lowZ+width; z++) {
				if (l.equals(new Location(l.getWorld(), x, baseY + 2, z))) {
					return true;
				}
			}
		}
		if (l.equals(new Location(l.getWorld(), lowX, baseY + 1, lowZ)) ||
		    l.equals(new Location(l.getWorld(), lowX, baseY + 1, lowZ+width)) ||
		    l.equals(new Location(l.getWorld(), lowX+length, baseY + 1, lowZ)) ||
		    l.equals(new Location(l.getWorld(), lowX+length, baseY + 1, lowZ+width))) {
			return true;
		}
		return false;
	}

}
