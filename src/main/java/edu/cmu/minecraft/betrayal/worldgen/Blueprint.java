package edu.cmu.minecraft.betrayal.worldgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Blueprint implements Materializable {

	private ArrayList<Entrance> doors;
	private Map<BlockFace, List<Wall>> wallsByDir;

	public Blueprint() {
		doors = new ArrayList<Entrance>();
		wallsByDir = new HashMap<>();
	}

	public ArrayList<Entrance> getDoors() {
		return doors;
	}

	public void addDoor(Entrance d) {
		doors.add(d);
	}

	public void addWall(Wall w) {
		wallsByDir.putIfAbsent(w.getFacing().getOppositeFace(), new ArrayList<>());
		wallsByDir.get(w.getFacing().getOppositeFace()).add(w);
	}
	
	/**
	 * Gets a list of walls that face south
	 * 
	 * @return List of south-facing walls
	 */
	public List<Wall> getNorthWalls() {
		return wallsByDir.getOrDefault(BlockFace.NORTH, new ArrayList<>());
	}

	/**
	 * Gets a list of walls that face north
	 * 
	 * @return List of north-facing walls
	 */
	public List<Wall> getSouthWalls() {
		return wallsByDir.getOrDefault(BlockFace.SOUTH, new ArrayList<>());
	}

	/**
	 * Gets a list of walls that face west
	 * 
	 * @return List of west-facing walls
	 */
	public List<Wall> getEastWalls() {
		return wallsByDir.getOrDefault(BlockFace.EAST, new ArrayList<>());
	}

	/**
	 * Gets a list of walls that face east
	 * 
	 * @return List of east-facing walls
	 */
	public List<Wall> getWestWalls() {
		return wallsByDir.getOrDefault(BlockFace.WEST, new ArrayList<>());
	}
	
	public List<Wall> getWallsByDir(BlockFace dir) {
		switch(dir) {
		case NORTH: return this.getNorthWalls();
		case SOUTH: return this.getSouthWalls();
		case EAST: return this.getEastWalls();
		case WEST: return this.getWestWalls();
		default: return new ArrayList<>();
		}
	}

	public Entrance getDoorByBlock(Block block) {
		for (Entrance door : doors) {
			if (door.includesBlock(block)) {
				return door;
			}
		}
		return null;
	}
	
	@Override
	public void materialize(World world) {
		for (List<Wall> walls : wallsByDir.values()) {
			for (Wall wall : walls) {
				wall.materialize(world);
			}
		}
		for (Entrance door : doors) {
			door.materialize(world);
		}
	}

	@Override
	public boolean contains(Location l) {
		for (List<Wall> walls : wallsByDir.values()) {
			for (Wall wall : walls) {
				if (wall.contains(l)) {
					return true;
				}
			}
		}
		for (Entrance e : doors) {
			if (e.contains(l)) {
				return true;
			}
		}
		return false;
	}

}
