package edu.cmu.minecraft.betrayal.worldgen.furniture;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Chest;

public class Trunk implements Furniture {

	private int x;
	private int y;
	private int z;
	private BlockFace facing;
	
	public Trunk(int x, int y, int z, BlockFace facing) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.facing = facing;
	}
	
	@Override
	public void materialize(World w) {
		Block b = w.getBlockAt(x, y, z);
		Chest c = new Chest(Material.CHEST);
		c.setFacingDirection(facing);
		b.setType(c.getItemType());
		BlockState state = b.getState();
		state.setData(c);
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
