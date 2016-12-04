package edu.cmu.minecraft.betrayal.worldgen.furniture;

import org.bukkit.Location;
import org.bukkit.Material;
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
		t.setFacingDirection(facing);
		Block b = w.getBlockAt(x, y, z);
//		b.setType(t.getItemType());
		BlockState state = b.getState();
		state.setData(t);
		state.update();
	}

	@Override
	public boolean contains(Location l) {
		if (l.equals(new Location(l.getWorld(), x, y, z))) {
			return true;
		}
		return false;
	}

}
