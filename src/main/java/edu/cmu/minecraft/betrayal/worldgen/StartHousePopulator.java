package edu.cmu.minecraft.betrayal.worldgen;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
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
		Blueprint bp = BlueprintFactory.getInstance(plugin).getBlueprint(chunk, null);
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
