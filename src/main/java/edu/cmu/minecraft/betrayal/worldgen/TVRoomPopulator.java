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
		if (blueprint.getWestWalls().size() < 1 || blueprint.getNorthWalls().size() < 1) {
			if (blueprint.getEastWalls().size() < 1 || blueprint.getSouthWalls().size() < 1) {
				logger.info("No walls in room??");
				return;
			}
			Wall east = blueprint.getEastWalls().get(0);
			int baseY = east.getLowCorner().getBlockY()-1;
			int ceilY = east.getHighCorner().getBlockY();
			int height = ceilY - baseY - 1;
			logger.info("baseY = " + baseY + "; ceilY = " + ceilY);
			int maxZ = Math.max(east.getLowCorner().getBlockZ(), east.getHighCorner().getBlockZ());
			int minZ = Math.min(east.getLowCorner().getBlockZ(), east.getHighCorner().getBlockZ());
			int width = maxZ - minZ - 1;
			int midZ = ((maxZ - minZ) / 2) + minZ;
			
			Wall south = blueprint.getSouthWalls().get(0);
			int maxX = Math.max(south.getLowCorner().getBlockX(), south.getHighCorner().getBlockX());
			int minX = Math.min(south.getLowCorner().getBlockX(), south.getHighCorner().getBlockX());
			blueprint.addFurniture(new Television(minX+3, baseY, minZ+3, width-6, height-2, BlockFace.SOUTH));
			for (int z = minZ + 7; z <= maxZ-2; z += 3) {
				logger.info("z = " + z + ";x = " + (minX+5) + "; width= " + (width-3));
				blueprint.addFurniture(new Sofa(minX+5, baseY, z, width-9, BlockFace.WEST));
			}
		} else {
			Wall west = blueprint.getWestWalls().get(0);
			int baseY = west.getLowCorner().getBlockY()-1;
			int ceilY = west.getHighCorner().getBlockY();
			int height = ceilY - baseY - 1;
			logger.info("baseY = " + baseY + "; ceilY = " + ceilY);
			int maxZ = Math.max(west.getLowCorner().getBlockZ(), west.getHighCorner().getBlockZ());
			int minZ = Math.min(west.getLowCorner().getBlockZ(), west.getHighCorner().getBlockZ());
			int width = maxZ - minZ - 1;
			int midZ = ((maxZ - minZ) / 2) + minZ;
			
			Wall north = blueprint.getNorthWalls().get(0);
			int maxX = Math.max(north.getLowCorner().getBlockX(), north.getHighCorner().getBlockX());
			int minX = Math.min(north.getLowCorner().getBlockX(), north.getHighCorner().getBlockX());
			blueprint.addFurniture(new Television(minX+3, baseY, minZ+3, width-6, height-2, BlockFace.SOUTH));
			for (int z = minZ + 7; z <= maxZ-2; z += 3) {
				logger.info("z = " + z + ";x = " + (minX+5) + "; width= " + (width-3));
				blueprint.addFurniture(new Sofa(minX+5, baseY, z, width-9, BlockFace.WEST));
			}
		}
	}

}
