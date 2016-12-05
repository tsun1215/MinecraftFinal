package edu.cmu.minecraft.betrayal.worldgen;

import java.util.logging.Logger;

import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;

import edu.cmu.minecraft.betrayal.worldgen.furniture.Table;
import edu.cmu.minecraft.betrayal.worldgen.furniture.Trunk;

public class DiningRoomPopulator implements FurniturePopulator {

	private static DiningRoomPopulator self = null;
	private Plugin plugin;
	private Logger logger;
	
	protected DiningRoomPopulator(Plugin p) {
		this.plugin = p;
		this.logger = plugin.getLogger();
	}
	
	public static DiningRoomPopulator getInstance(Plugin p) {
		if (self == null) {
			self = new DiningRoomPopulator(p);
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
		int tableWidth = width-8;
		blueprint.addFurniture(new Table(minX+4, baseY, minZ+4, tableWidth, tableWidth));
		blueprint.addFurniture(new Trunk(minX+4+(tableWidth/2), baseY+3, minZ+4+(tableWidth/2), BlockFace.EAST));
		blueprint.addFurniture(new Trunk(minX+4+(tableWidth/2), baseY+3, minZ+5+(tableWidth/2), BlockFace.EAST));
		blueprint.addFurniture(new Trunk(minX+4+(tableWidth/2), baseY+3, minZ+6+(tableWidth/2), BlockFace.EAST));
		blueprint.addFurniture(new Trunk(minX+4+(tableWidth/2), baseY+3, minZ+7+(tableWidth/2), BlockFace.EAST));


	}

}
