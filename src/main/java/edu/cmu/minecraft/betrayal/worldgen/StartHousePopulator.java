package edu.cmu.minecraft.betrayal.worldgen;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.Plugin;

import edu.cmu.minecraft.betrayal.entities.Room;

public class StartHousePopulator extends BlockPopulator {
	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_HEIGHT = 16;
	public static final int CEILING_HEIGHT = 6;
	
	private Logger logger;
	
	public StartHousePopulator(Plugin plugin) {
		super();
		this.logger = plugin.getLogger();
	}
	
	private void generateHouse(World world, Chunk chunk) {
		int cX = chunk.getX() * CHUNK_WIDTH;
		int cZ = chunk.getZ() * CHUNK_HEIGHT;
		
		Room room = new Room(world, cX, cX + CHUNK_WIDTH, cZ, cZ + CHUNK_HEIGHT, CEILING_HEIGHT, Material.BRICK, logger);
		room.generateRoom();
	}

	@Override
	public void populate(World world, Random random, Chunk source) {
		int cX = source.getX() * 16;
		int cZ = source.getZ() * 16;
		if (-100 < cX && cX < 100 && -100 < cZ && cZ < 100) {
			if (random.nextInt(100) > 90) {
				//generateHouse(world, source);
			}
		}
	}
	

}
