package edu.cmu.minecraft.betrayal.worldgen;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.Plugin;

import edu.cmu.minecraft.betrayal.worldgen.furniture.EndTable;
import edu.cmu.minecraft.betrayal.worldgen.furniture.Sofa;
import edu.cmu.minecraft.betrayal.worldgen.furniture.Table;
import edu.cmu.minecraft.betrayal.worldgen.furniture.Television;
import edu.cmu.minecraft.betrayal.worldgen.furniture.Trunk;
import edu.cmu.minecraft.betrayal.worldgen.furniture.WallTorch;

public class StartHousePopulator extends BlockPopulator {
	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_HEIGHT = 16;
	public static final int CEILING_HEIGHT = 6;

	private Plugin plugin;

	public StartHousePopulator(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	private void generateHouse(World world, Chunk chunk) {
		int cX = chunk.getX() * CHUNK_WIDTH;
		int cZ = chunk.getZ() * CHUNK_HEIGHT;

		int highest = -1;
		for (int offsetX = 0; offsetX < CHUNK_WIDTH; offsetX++) {
			for (int offsetZ = 0; offsetZ < CHUNK_HEIGHT; offsetZ++) {
				int x = cX + offsetX;
				int z = cZ + offsetZ;
				if (world.getHighestBlockYAt(x, z) > highest) {
					highest = world.getHighestBlockYAt(x, z);
				}
			}
		}

		Blueprint bp = new Blueprint();
		// TODO: Clean up this mess
		// North Wall
		bp.addWall(new Wall(Material.BRICK, 
				new Location(world, cX, highest, cZ),
				new Location(world, cX + CHUNK_WIDTH - 1, highest + CEILING_HEIGHT, cZ),
				BlockFace.SOUTH));
		// South Wall
		bp.addWall(new Wall(Material.BRICK, 
				new Location(world, cX, highest,cZ + CHUNK_HEIGHT - 1), 
				new Location(world, cX + CHUNK_WIDTH - 1, highest + CEILING_HEIGHT, cZ + CHUNK_HEIGHT - 1), 
				BlockFace.NORTH));
		// West Wall
		bp.addWall(new Wall(Material.BRICK, 
				new Location(world, cX, highest, cZ), 
				new Location(world, cX, highest + CEILING_HEIGHT, cZ + CHUNK_HEIGHT - 1),
				BlockFace.EAST));
		// East Wall
		bp.addWall(new Wall(Material.BRICK, 
				new Location(world, cX + CHUNK_WIDTH - 1, highest, cZ), 
				new Location(world, cX + CHUNK_WIDTH - 1, highest + CEILING_HEIGHT, cZ + CHUNK_HEIGHT - 1), 
				BlockFace.WEST));

		/* Doors */
		bp.addDoor(new Entrance(plugin, new Location(world, cX + 7, highest, cZ),
				Material.DARK_OAK_DOOR, BlockFace.NORTH, true));
		bp.addDoor(new Entrance(plugin, new Location(world, cX, highest, cZ + 7),
				Material.DARK_OAK_DOOR, BlockFace.WEST, true));
		bp.addDoor(new Entrance(plugin, new Location(world, cX + CHUNK_WIDTH - 1, highest, cZ + 7),
				Material.DARK_OAK_DOOR, BlockFace.EAST, true));
		bp.addDoor(new Entrance(plugin, new Location(world, cX + 7, highest, cZ + CHUNK_HEIGHT - 1),
				Material.DARK_OAK_DOOR, BlockFace.SOUTH, true));

		bp.addFurniture(new Table(cX+2, highest-1, cZ+1, 3, 3));
		bp.addFurniture(new EndTable(cX+10, highest-1, cZ+10));
		bp.addFurniture(new Sofa(cX+6, highest-1, cZ+6, 2, BlockFace.EAST));
		bp.addFurniture(new Sofa(cX+10, highest-1, cZ+10, 2, BlockFace.WEST));
		bp.addFurniture(new Television(cX+12, highest-1, cZ+12, 2, 2, BlockFace.EAST));
		//bp.addFurniture(new WallTorch(cX, highest+5, cZ+1, BlockFace.EAST));
		bp.addFurniture(new Trunk(cX+1, highest+1, cZ+1, BlockFace.EAST));
		bp.addFurniture(new Trunk(cX+2, highest+1, cZ+1, BlockFace.EAST));
		RoomManager.getInstance().addBlueprint(chunk, bp);
		bp.materialize(world);
	}

	@Override
	public void populate(World world, Random random, Chunk source) {
		int cX = source.getX() * 16;
		int cZ = source.getZ() * 16;
		if (cX == 0 && cZ == 0) { // Generate only the start house
			generateHouse(world, source);
		}
	}

}
