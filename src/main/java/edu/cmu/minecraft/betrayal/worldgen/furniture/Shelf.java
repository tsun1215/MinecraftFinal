package edu.cmu.minecraft.betrayal.worldgen.furniture;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Shelf implements Furniture {

	private int lowX;
	private int baseY;
	private int lowZ;
	private int height;
	private int width;
	private boolean isNS;
	private ArrayList<Location> locs;
	
	public Shelf(int lowX, int baseY, int lowZ, int height, int width, boolean isNS) {
		this.lowX = lowX;
		this.baseY = baseY;
		this.lowZ = lowZ;
		this.height = height;
		this.width = width;
		this.isNS = isNS;
		this.locs = new ArrayList<Location>();
	}
	
	@Override
	public void materialize(World w) {
		if (isNS) {
			for (int z = lowZ; z <= lowZ + width; z++) {
				for (int y = baseY+1; y <= baseY+height+1; y++) {
					w.getBlockAt(lowX, y, z).setType(Material.BOOKSHELF);
					locs.add(new Location(w, lowX, y, z));
				}
			}
		} else {
			for (int x = lowX; x <= lowX + width; x++) {
				for (int y = baseY+1; y <= baseY+height+1; y++) {
					w.getBlockAt(x, y, lowZ).setType(Material.BOOKSHELF);
					locs.add(new Location(w, x, y, lowZ));
				}
			}
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
