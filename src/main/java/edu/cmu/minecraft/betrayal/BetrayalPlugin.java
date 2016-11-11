package edu.cmu.minecraft.betrayal;

import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
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
		this.getServer().getPluginManager().registerEvents(new DoorListener(this), this);
		this.getCommand(EnterWorldCommand.COMMAND_NAME).setExecutor(new EnterWorldCommand(this));
	}
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new FlatLandGenerator(this);
	}
	
	public static class DoorListener implements Listener {
		private Plugin plugin;
		private Logger logger;
		
		public DoorListener(Plugin plugin) {
			this.plugin = plugin;
			this.logger = this.plugin.getLogger();
		}
		
		// TODO: Extract this class and also maybe migrate to PlayerInteractEntityEvent
		@EventHandler
		public void onPlayerDoorOpen(PlayerInteractEvent event) {
			Action action = event.getAction();
			Block block = event.getClickedBlock();
			if (block != null) {
				logger.info("Block: " + block);
				logger.info("Chunk: " + block.getChunk());
				Blueprint b = RoomManager.getInstance().getBlueprint(block.getChunk());
				if (b != null) {
					if (action == Action.RIGHT_CLICK_BLOCK) {
						Entrance door = b.getDoorByBlock(block);
						if (door != null) {
							door.open(); //Generate room here?
							this.logger.info("Clicked on door");
						}
					}				
				} else {
					this.logger.info("Unable to find chunk at: " + block.getChunk());
				}
			}
		}
	}
}
