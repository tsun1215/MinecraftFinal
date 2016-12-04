package edu.cmu.minecraft.betrayal.worldgen;

import org.bukkit.Location;
import org.bukkit.World;

public interface Materializable {
	
	/**
	 * Creates the object/structure in Minecraft
	 * 
	 * @param w World to materialize in
	 */
	public void materialize(World w);
	
	
	/**
	 * Returns true if the location is within the object
	 * @param l Location to check against
	 * @return true if l is contained within this object
	 */
	public boolean contains(Location l);
}
