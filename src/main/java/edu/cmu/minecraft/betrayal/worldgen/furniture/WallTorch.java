package edu.cmu.minecraft.betrayal.worldgen.furniture;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Torch;

public class WallTorch implements Furniture {

	private int x;
	private int y;
	private int z;
	private BlockFace facing;
	
	public WallTorch(int x, int y, int z, BlockFace facing) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.facing = facing;
	}
	
	@Override
	public void materialize(World w) {
		Torch t = new Torch();
		Block block = w.getBlockAt(x, y, z);
		BlockState state = block.getState();
		t.setFacingDirection(facing);
		// Turns out you need to set state's type for torches
		state.setType(t.getItemType());
		state.setData(t);
		state.update(true);
	}

	@Override
	public boolean contains(Location l) {
		if (l.equals(new Location(l.getWorld(), x, y, z))) {
			return true;
		}
		return false;
	}

}
