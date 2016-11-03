package edu.cmu.minecraft.betrayal.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import edu.cmu.minecraft.betrayal.BetrayalPlugin;
import edu.cmu.minecraft.betrayal.worldgen.FlatLandGenerator;

public class EnterWorldCommand implements CommandExecutor {
	private Plugin plugin;
	private Logger logger;
	public static final String COMMAND_NAME = "enter";
	
	public EnterWorldCommand(Plugin plugin) {
		super();
		this.plugin = plugin;
		this.logger = plugin.getLogger();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			// Create world if it doesn't exist
			WorldCreator wc = new WorldCreator(BetrayalPlugin.WORLD_NAME);
			wc.generator(new FlatLandGenerator(plugin));
			wc.environment(World.Environment.NORMAL);
			World w = wc.createWorld();
			
			// Disallow spawning of mobs or animals
			w.setSpawnFlags(false, false);
			
			// Teleport current player to location
			logger.info("Teleporting " + player.getName() + " to " + BetrayalPlugin.WORLD_NAME);
			player.teleport(new Location(w, 0, 15, 100));
			player.sendMessage("Welcome to " + ChatColor.RED + BetrayalPlugin.WORLD_NAME + ChatColor.RESET + "!");
		} else {
			// TODO: Allow an admin command to send a given player to the world
			logger.warning("Invalid use of EnterWorldCommand: Not a player");
		}
		return true;
	}

}
