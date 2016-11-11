package edu.cmu.minecraft.betrayal.worldgen;

import java.util.ArrayList;

import org.bukkit.block.Block;

public class Blueprint implements Materializable {

	private ArrayList<Entrance> doors;
	private ArrayList<Wall> walls;

	public Blueprint() {
		doors = new ArrayList<Entrance>();
		walls = new ArrayList<Wall>();
	}

	public ArrayList<Entrance> getDoors() {
		return doors;
	}

	public void addDoor(Entrance d) {
		doors.add(d);
	}

	public ArrayList<Wall> getWalls() {
		return walls;
	}

	public void addWall(Wall w) {
		walls.add(w);
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
	public void materialize() {
		for (Wall wall : walls) {
			wall.materialize();
		}
		for (Entrance door : doors) {
			door.materialize();
		}
	}

}
