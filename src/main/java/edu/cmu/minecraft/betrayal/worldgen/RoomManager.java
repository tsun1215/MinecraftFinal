package edu.cmu.minecraft.betrayal.worldgen;

import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public class RoomManager {

	private static RoomManager self = null;
	private HashMap<Chunk, Blueprint> rooms;

	public RoomManager() {
		rooms = new HashMap<>();
	}

	public static RoomManager getInstance() {
		if (self == null) {
			self = new RoomManager();
		}
		return self;
	}

	public void addBlueprint(Chunk c, Blueprint b) {
		rooms.put(c, b);
	}

	public Blueprint getBlueprint(Chunk c) {
		return rooms.get(c);
	}
	
	public Blueprint getBlueprintRelative(Chunk c, BlockFace dir) {
		int cX = c.getX() + dir.getModX();
		int cZ = c.getZ() + dir.getModZ();
		World world = c.getWorld();
		
		return rooms.get(world.getChunkAt(cX, cZ));
	}
}
