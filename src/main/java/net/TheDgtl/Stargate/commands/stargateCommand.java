/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.TheDgtl.Stargate.commands;

import net.TheDgtl.Stargate.EconomyHandler;
import net.TheDgtl.Stargate.Gate;
import net.TheDgtl.Stargate.Portal;
import static net.TheDgtl.Stargate.Stargate.activeList;
import static net.TheDgtl.Stargate.Stargate.enableBungee;
import static net.TheDgtl.Stargate.Stargate.log;
import static net.TheDgtl.Stargate.Stargate.managedWorlds;
import static net.TheDgtl.Stargate.Stargate.openList;
import net.TheDgtl.Stargate.pmListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Frostalf
 */
public class stargateCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmd = command.getName();
		if (cmd.equalsIgnoreCase("sg")) {
			if (args.length != 1) return false;
			if (args[0].equalsIgnoreCase("about")) {
				sender.sendMessage("Stargate Plugin created by Drakia");
				if (!lang.getString("author").isEmpty())
					sender.sendMessage("Language created by " + lang.getString("author"));
				return true;
			}
			if (sender instanceof Player) {
				Player p = (Player)sender;
				if (!hasPerm(p, "stargate.admin") && !hasPerm(p, "stargate.admin.reload")) {
					sendMessage(sender, "Permission Denied");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("reload")) {
				// Deactivate portals
				for (Portal p : activeList) {
					p.deactivate();
				}
				// Close portals
				closeAllPortals();
				// Clear all lists
				activeList.clear();
				openList.clear();
				managedWorlds.clear();
				Portal.clearGates();
				Gate.clearGates();

				// Store the old Bungee enabled value
				boolean oldEnableBungee = enableBungee;
				// Reload data
				loadConfig();
				loadGates();
				loadAllPortals();
				lang.setLang(langName);
				lang.reload();

				// Load Economy support if enabled/clear if disabled
				if (EconomyHandler.economyEnabled && EconomyHandler.economy == null) {
					if (EconomyHandler.setupEconomy(pm)) {
						if (EconomyHandler.economy != null)
							log.info("[Stargate] Vault v" + EconomyHandler.vault.getDescription().getVersion() + " found");
			        }
				}
				if (!EconomyHandler.economyEnabled) {
					EconomyHandler.vault = null;
					EconomyHandler.economy = null;
				}

				// Enable the required channels for Bungee support
				if (oldEnableBungee != enableBungee) {
					if (enableBungee) {
						Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
						Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new pmListener());
					} else {
						Bukkit.getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");
						Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
					}
				}

				sendMessage(sender, "Stargate reloaded");
				return true;
			}
			return false;
		}
		return false;
	}
}
