package edu.cmu.minecraft.betrayal;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import edu.cmu.minecraft.betrayal.commands.EnterWorldCommand;
import edu.cmu.minecraft.betrayal.worldgen.FlatLandGenerator;

public class BetrayalPlugin extends JavaPlugin {
	public static final String WORLD_NAME = "BetrayalWorld";

	@Override
	public void onEnable() {
		this.getCommand("test").setExecutor(new EnterWorldCommand(this));
	}
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new FlatLandGenerator(this);
	}
}
