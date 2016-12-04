package edu.cmu.minecraft.betrayal.worldgen;

import org.bukkit.Chunk;
import org.bukkit.plugin.Plugin;

public class BlueprintFactory {

	private static BlueprintFactory self = null;
	private Plugin plugin;
	private WallPopulator wallPopulator;

	protected BlueprintFactory(Plugin p) {
		this.plugin = p;
		this.wallPopulator = new WallPopulator(p);
	}

	public static BlueprintFactory getInstance(Plugin p) {
		if (self == null) {
			self = new BlueprintFactory(p);
		}
		return self;
	}

	public Blueprint getBlueprint(Chunk chunk, Entrance enteredDoor) {
		Blueprint bp = new Blueprint();
		wallPopulator.populate(bp, chunk);
		TorchPopulator.getInstance(plugin).populate(bp);
		TVRoomPopulator.getInstance(plugin).populate(bp);
		return bp;
	}
}
