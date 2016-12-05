package edu.cmu.minecraft.betrayal.worldgen;

import java.util.logging.Logger;

import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;

import edu.cmu.minecraft.betrayal.worldgen.furniture.Sofa;
import edu.cmu.minecraft.betrayal.worldgen.furniture.Television;

public class TVRoomPopulator implements FurniturePopulator {

	private static TVRoomPopulator self = null;
	private Plugin plugin;
	private Logger logger;
	
	protected TVRoomPopulator(Plugin p) {
		this.plugin = p;
		this.logger = plugin.getLogger();
	}
	
	public static TVRoomPopulator getInstance(Plugin p) {
		if (self == null) {
			self = new TVRoomPopulator(p);
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
			logger.warning("Couldn't make a TV room - no side walls");
			return;
		}
		int baseY = side.getLowCorner().getBlockY()-1;
		int ceilY = side.getHighCorner().getBlockY();
		int height = ceilY - baseY - 1;
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
		blueprint.addFurniture(new Television(minX+3, baseY, minZ+3, width-6, height-2, BlockFace.SOUTH));
		for (int z = minZ + 7; z <= maxZ-2; z += 3) {
			blueprint.addFurniture(new Sofa(minX+5, baseY, z, width-9, BlockFace.WEST));
		}
	}

}
