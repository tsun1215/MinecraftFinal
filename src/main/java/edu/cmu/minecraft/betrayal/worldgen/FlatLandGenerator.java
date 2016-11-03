package edu.cmu.minecraft.betrayal.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

public class FlatLandGenerator extends ChunkGenerator {
	public static final int BEDROCK_HEIGHT = 1;
	public static final int STONE_HEIGHT = 10;
	public static final int DIRT_HEIGHT = 2;
	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_HEIGHT = 16;
	
	private Plugin plugin;
	
	public FlatLandGenerator(Plugin plugin) {
		super();
		this.plugin = plugin;
	}
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		List<BlockPopulator> populators = new ArrayList<>();
		populators.add(new StartHousePopulator(plugin));
		return populators;
	}

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunk = this.createChunkData(world);
		int layerY = 0;
		// Fill bottom with bedrock
		fillToHeight(chunk, Material.BEDROCK, layerY, layerY + BEDROCK_HEIGHT);
		layerY += BEDROCK_HEIGHT;
		
		// Fill a stone layer
		fillToHeight(chunk, Material.STONE, layerY, layerY + STONE_HEIGHT);
		layerY += STONE_HEIGHT;
		
		// Fill top with dirt
		fillToHeight(chunk, Material.DIRT, layerY, layerY + DIRT_HEIGHT);
		layerY += DIRT_HEIGHT;
		
		return chunk;
	}
	
	/**
	 * Fills a chunk with a layer of height (endY - startY) with the given material
	 * 
	 * @param chunk {@link ChunkData} to generate layers for
	 * @param material {@link Material} to fill the layer with
	 * @param startY Y-coordinate (inclusive) to start filling from
	 * @param endY Y-coordinate (exclusive) to fill until
	 */
	private void fillToHeight(ChunkData chunk, Material material, int startY, int endY) {
		chunk.setRegion(0, startY, 0, CHUNK_WIDTH, endY, CHUNK_HEIGHT, material);
	}
	
}
