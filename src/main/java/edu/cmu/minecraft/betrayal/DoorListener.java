package edu.cmu.minecraft.betrayal;

import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import edu.cmu.minecraft.betrayal.worldgen.Blueprint;
import edu.cmu.minecraft.betrayal.worldgen.BlueprintFactory;
import edu.cmu.minecraft.betrayal.worldgen.Entrance;
import edu.cmu.minecraft.betrayal.worldgen.RoomManager;

public class DoorListener implements Listener {
	private Plugin plugin;
	private Logger logger;

	public DoorListener(Plugin plugin) {
		this.plugin = plugin;
		this.logger = this.plugin.getLogger();
	}

	@EventHandler
	public void onPlayerDoorOpen(PlayerInteractEvent event) {
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		if (block != null) {
			Chunk currChunk = block.getChunk();
			Blueprint b = RoomManager.getInstance().getBlueprint(currChunk);
			if (b != null) {
				if (action == Action.RIGHT_CLICK_BLOCK) {
					Entrance door = b.getDoorByBlock(block);
					if (door != null) {
						World world = currChunk.getWorld();
						RoomManager rm = RoomManager.getInstance();
						int nextChunkX = currChunk.getX()
								+ door.getFacing().getModX();
						int nextChunkZ = currChunk.getZ()
								+ door.getFacing().getModZ();
						Chunk nextChunk = world.getChunkAt(nextChunkX,
								nextChunkZ);

						// Only generate room if it doesn't already exist
						if (rm.getBlueprint(nextChunk) == null) {
							Blueprint bp = BlueprintFactory.getInstance(plugin)
									.getBlueprint(nextChunk, door);
							RoomManager.getInstance().addBlueprint(nextChunk,
									bp);
							bp.materialize(world);
						}
					}
				}
			} else {
				this.logger
						.info("Unable to find chunk at: " + block.getChunk());
			}
		}
	}
}