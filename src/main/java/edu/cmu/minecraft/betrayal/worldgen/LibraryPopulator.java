package edu.cmu.minecraft.betrayal.worldgen;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import edu.cmu.minecraft.betrayal.worldgen.furniture.Shelf;

public class LibraryPopulator implements FurniturePopulator {

	private static LibraryPopulator self = null;
	private Plugin plugin;
	private Logger logger;
	
	protected LibraryPopulator(Plugin p) {
		this.plugin = p;
		this.logger = plugin.getLogger();
	}
	
	public static LibraryPopulator getInstance(Plugin p) {
		if (self == null) {
			self = new LibraryPopulator(p);
		}
		return self;
	}
	
	@Override
	public void populate(Blueprint blueprint) {
		Wall side;
		if (blueprint.getWestWalls().size() > 0) {
			side = blueprint.getWestWalls().get(0);
		} else if (blueprint.getEastWalls().size() > 0){
			side = blueprint.getEastWalls().get(0);
		} else {
			logger.warning("Couldn't make a Library - no side walls");
			return;
		}
		int baseY = side.getLowCorner().getBlockY()-1;
		int ceilY = side.getHighCorner().getBlockY();
		int height = ceilY - baseY - 4;
		int maxZ = Math.max(side.getLowCorner().getBlockZ(), side.getHighCorner().getBlockZ());
		int minZ = Math.min(side.getLowCorner().getBlockZ(), side.getHighCorner().getBlockZ());
		int width = maxZ - minZ - 1;
		
		int minX;
		if (blueprint.getNorthWalls().size() > 0) {
			Wall north = blueprint.getNorthWalls().get(0);
			minX = Math.min(north.getLowCorner().getBlockX(), north.getHighCorner().getBlockX());
		} else if (blueprint.getSouthWalls().size() > 0) {
			Wall south = blueprint.getSouthWalls().get(0);
			minX = Math.min(south.getLowCorner().getBlockX(), south.getHighCorner().getBlockX());
		} else {
			minX = Math.min(side.getLowCorner().getBlockX(), side.getHighCorner().getBlockX());
		}
		for (int z = minZ+3; z <= maxZ-2; z+= 3) {
			blueprint.addFurniture(new Shelf(minX+2, baseY, z, height, width/3-1, false));
			blueprint.addFurniture(new Shelf(minX+9, baseY, z, height, width/3-1, false));

		}
	}

}
