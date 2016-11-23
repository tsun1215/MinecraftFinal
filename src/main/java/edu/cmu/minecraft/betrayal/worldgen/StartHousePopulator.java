package edu.cmu.minecraft.betrayal.worldgen;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.Plugin;

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
		bp.addWall(new Wall(world, Material.BRICK, cX, highest, cZ,
				cX + CHUNK_WIDTH - 1, highest + CEILING_HEIGHT, cZ));
		// South Wall
		bp.addWall(new Wall(world, Material.BRICK, cX, highest,
				cZ + CHUNK_HEIGHT - 1, cX + CHUNK_WIDTH - 1,
				highest + CEILING_HEIGHT, cZ + CHUNK_HEIGHT - 1));
		// West Wall
		bp.addWall(new Wall(world, Material.BRICK, cX, highest, cZ, cX,
				highest + CEILING_HEIGHT, cZ + CHUNK_HEIGHT - 1));
		// East Wall
		bp.addWall(new Wall(world, Material.BRICK, cX + CHUNK_WIDTH - 1,
				highest, cZ, cX + CHUNK_WIDTH - 1, highest + CEILING_HEIGHT,
				cZ + CHUNK_HEIGHT - 1));

		/* Doors */
		bp.addDoor(new Entrance(plugin, world, Material.DARK_OAK_DOOR,
				BlockFace.NORTH, true, cX + 7, highest, cZ));
		bp.addDoor(new Entrance(plugin, world, Material.DARK_OAK_DOOR,
				BlockFace.EAST, true, cX, highest, cZ + 7));
		bp.addDoor(new Entrance(plugin, world, Material.DARK_OAK_DOOR,
				BlockFace.WEST, true, cX + CHUNK_WIDTH - 1, highest, cZ + 7));
		bp.addDoor(new Entrance(plugin, world, Material.DARK_OAK_DOOR,
				BlockFace.SOUTH, true, cX + 7, highest, cZ + CHUNK_HEIGHT - 1));

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
