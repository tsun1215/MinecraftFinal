package edu.cmu.minecraft.betrayal.worldgen;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Wall implements Materializable {

	final private World world;
	// Corners describe walls inclusively (corners lie within the wall)
	final private Block lowCorner; 
	final private Block highCorner; // Corner diagonally opposite lowCorner
	final private Material material;
	
	public Wall(World w, Material m, int lowX, int lowY, int lowZ, int highX, int highY, int highZ) {
		world = w;
		material = m;
		lowCorner = world.getBlockAt(lowX, lowY, lowZ);
		highCorner = world.getBlockAt(highX, highY, highZ);
	}
	
	public Block getLowCorner() {
		return lowCorner;
	}
	
	public Block getHighCorner() {
		return highCorner;
	}
	
	public Material getMaterial() {
		return material;
	}

	@Override
	public void materialize() {
		for (int x = lowCorner.getX(); x <= highCorner.getX(); x++) {
			for (int y = lowCorner.getY(); y <= highCorner.getY(); y++) {
				for (int z = lowCorner.getZ(); z <= highCorner.getZ(); z++) {
					this.world.getBlockAt(x, y, z).setType(this.material);
				}
			}
		}
	}
	
}
