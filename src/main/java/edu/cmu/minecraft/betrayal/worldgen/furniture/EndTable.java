package edu.cmu.minecraft.betrayal.worldgen.furniture;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class EndTable implements Furniture {

	private int x;
	private int z;
	private int baseY;
	
	public EndTable(int x, int baseY, int z) {
		this.x = x;
		this.baseY = baseY;
		this.z = z;
	}
	
	@Override
	public void materialize(World w) {
		w.getBlockAt(x, baseY + 1, z).setType(Material.BIRCH_FENCE);
		w.getBlockAt(x, baseY + 2, z).setType(Material.WOOD_PLATE);
	}

	@Override
	public boolean contains(Location l) {
		if (l.equals(new Location(l.getWorld(), x, baseY+1, z)) ||
		   (l.equals(new Location(l.getWorld(), x, baseY+2, z)))) {
			return true;
		}
		return false;
	}

}
