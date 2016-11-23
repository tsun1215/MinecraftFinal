package edu.cmu.minecraft.betrayal.worldgen.furniture;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

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
		w.getBlockAt(lowX, baseY + 1, lowZ).setType(Material.ACACIA_FENCE);
		w.getBlockAt(lowX, baseY + 1, lowZ+width).setType(Material.ACACIA_FENCE);
		w.getBlockAt(lowX+length, baseY + 1, lowZ).setType(Material.ACACIA_FENCE);
		w.getBlockAt(lowX+length, baseY + 1, lowZ+width).setType(Material.ACACIA_FENCE);
		
		for (int x = lowX; x <= lowX+length; x++) {
			for (int z = lowZ; z <= lowZ+width; z++) {
				w.getBlockAt(x, baseY + 2, z).setType(Material.WOOD_STEP);
			}
		}
	}

	@Override
	public boolean contains(Location l) {
		// TODO Auto-generated method stub
		return false;
	}

}
