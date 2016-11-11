package edu.cmu.minecraft.betrayal;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import edu.cmu.minecraft.betrayal.commands.EnterWorldCommand;
import edu.cmu.minecraft.betrayal.worldgen.Blueprint;
import edu.cmu.minecraft.betrayal.worldgen.Entrance;
import edu.cmu.minecraft.betrayal.worldgen.FlatLandGenerator;
import edu.cmu.minecraft.betrayal.worldgen.RoomManager;

public class BetrayalPlugin extends JavaPlugin {
	public static final String WORLD_NAME = "BetrayalWorld";
	
	@Override
	public void onEnable() {
		this.getCommand(EnterWorldCommand.COMMAND_NAME).setExecutor(new EnterWorldCommand(this));
	}
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new FlatLandGenerator(this);
	}
	
	@EventHandler
	public void onPlayerDoorOpen(PlayerInteractEvent event) {
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		Blueprint b = RoomManager.getInstance().getBlueprint(block.getChunk());
		if (action == Action.RIGHT_CLICK_BLOCK) {
			Entrance door = b.getDoorByBlock(block);
			if (door != null) {
				door.open(); //Generate room here?
				this.getLogger().info("Clicked on door");
			}
		}
	}
}
