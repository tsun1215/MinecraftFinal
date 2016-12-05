package edu.cmu.minecraft.betrayal.worldgen;

import java.util.stream.Stream;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;

public class WallPopulator {
	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_HEIGHT = 16;
	public static final int CEILING_HEIGHT = 6;
	
	private Plugin plugin;
	
	public WallPopulator(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void populate(Blueprint bp, Chunk chunk) {
		World world = chunk.getWorld();
		RoomManager rm = RoomManager.getInstance();
		int cX = chunk.getX() * CHUNK_WIDTH;
		int cZ = chunk.getZ() * CHUNK_HEIGHT;
		
		// Determine highest point
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

		final int floorY = highest;

		
		// Add walls
		Stream.of(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST,
				BlockFace.WEST).forEach(dir -> {
					Blueprint adjRoom = rm.getBlueprintRelative(chunk, dir);
					// Only add a wall if there is not an adjacent wall
					if (adjRoom == null || (adjRoom != null && adjRoom
							.getWallsByDir(dir.getOppositeFace()).isEmpty())) {
						// Compute wall coords
						int wallStartX = (dir == BlockFace.EAST)
								? CHUNK_WIDTH - 1 : 0;
						int wallStartZ = (dir == BlockFace.SOUTH)
								? CHUNK_HEIGHT - 1 : 0;
						int wallEndX = (dir.getModX() == 0)
								? wallStartX + CHUNK_WIDTH - 1 : wallStartX;
						int wallEndZ = (dir.getModZ() == 0)
								? wallStartZ + CHUNK_HEIGHT - 1 : wallStartZ;
						Location wallStart = new Location(world,
								cX + wallStartX, floorY, cZ + wallStartZ);
						Location wallEnd = new Location(world, cX + wallEndX,
								floorY + CEILING_HEIGHT, cZ + wallEndZ);

						// Compute door coords
						int doorX = (wallStartX == wallEndX) ? wallStartX
								: (wallStartX + wallEndX) / 2;
						int doorZ = (wallStartZ == wallEndZ) ? wallStartZ
								: (wallStartZ + wallEndZ) / 2;
						Location doorLoc = new Location(world, cX + doorX,
								floorY, cZ + doorZ);

						// Add wall and door to blueprint
						bp.addWall(new Wall(Material.BRICK, wallStart, wallEnd,
								dir.getOppositeFace()));
						bp.addDoor(new Entrance(plugin, doorLoc,
								Material.DARK_OAK_DOOR, dir, true));
					}
				});
		
		// Add Ceiling
		bp.addWall(
				new Wall(Material.BRICK,
						new Location(world, cX, floorY + CEILING_HEIGHT, cZ),
						new Location(world, cX + CHUNK_WIDTH - 1,
								floorY + CEILING_HEIGHT, cZ + CHUNK_HEIGHT - 1),
						BlockFace.DOWN));
	}
}
