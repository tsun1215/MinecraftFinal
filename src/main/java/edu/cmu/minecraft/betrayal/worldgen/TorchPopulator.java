package edu.cmu.minecraft.betrayal.worldgen;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;

import edu.cmu.minecraft.betrayal.worldgen.furniture.WallTorch;

public class TorchPopulator implements FurniturePopulator {

	private static TorchPopulator self = null;
	private Plugin plugin;
	
	protected TorchPopulator(Plugin p) {
		this.plugin = p;
	}
	
	public static TorchPopulator getInstance(Plugin p) {
		if (self == null) {
			self = new TorchPopulator(p);
		}
		return self;
	}
	
	public void populate(Blueprint blueprint) {
		// Line of torches on walls
		List<Wall> walls = blueprint.getEastWalls();
		for (Wall wall : walls) {
			int highY = wall.getHighCorner().getBlockY();
			int lowY = wall.getLowCorner().getBlockY();
			int torchY = (5 * ((highY - lowY) / 6)) + lowY;
			int minZ = Math.min(wall.getLowCorner().getBlockZ(), wall.getHighCorner().getBlockZ());
			int maxZ = Math.max(wall.getLowCorner().getBlockZ(), wall.getHighCorner().getBlockZ());
			plugin.getLogger().info("min = " + minZ + "; max = " + maxZ + "; y = " + torchY);
			blueprint.addFurniture(new WallTorch(wall.getLowCorner().getBlockX()-1, torchY, minZ+1, BlockFace.WEST));
			blueprint.addFurniture(new WallTorch(wall.getLowCorner().getBlockX()-1, torchY, maxZ-1, BlockFace.WEST));

		}
		
		walls = blueprint.getWestWalls();
		for (Wall wall : walls) {
			int highY = wall.getHighCorner().getBlockY();
			int lowY = wall.getLowCorner().getBlockY();
			int torchY = (5 * ((highY - lowY) / 6)) + lowY;
			int minZ = Math.min(wall.getLowCorner().getBlockZ(), wall.getHighCorner().getBlockZ());
			int maxZ = Math.max(wall.getLowCorner().getBlockZ(), wall.getHighCorner().getBlockZ());
			blueprint.addFurniture(new WallTorch(wall.getLowCorner().getBlockX()+1, torchY, minZ+1, BlockFace.EAST));
			blueprint.addFurniture(new WallTorch(wall.getLowCorner().getBlockX()+1, torchY, maxZ-1, BlockFace.EAST));
		}
		
		for (Entrance entrance : blueprint.getDoors()) {
			Block base = entrance.getLoc().getBlock();
			Block t = base.getRelative(BlockFace.UP).getRelative(BlockFace.UP);
			int x = entrance.getFacing().getOppositeFace().getModX() + t.getX();
			int z = entrance.getFacing().getOppositeFace().getModZ() + t.getZ();
			blueprint.addFurniture(new WallTorch(x, t.getY(), z, entrance.getFacing().getOppositeFace()));
			if (entrance.isDoubleDoor()) {
				x = x + entrance.getRightDoorDirection().getModX();
				z = z + entrance.getRightDoorDirection().getModZ();
				blueprint.addFurniture(new WallTorch(x, t.getY(), z, entrance.getFacing().getOppositeFace()));
			}
		}

	}
	
}
