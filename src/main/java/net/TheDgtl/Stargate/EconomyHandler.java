package net.TheDgtl.Stargate;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Stargate - A portal plugin for Bukkit
 * Copyright (C) 2011, 2012 Steven "Drakia" Scott <Contact@TheDgtl.net>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author PseudoKnight (github.com/PseudoKnight)
 */

public class EconomyHandler {
	public static boolean economyEnabled = false;
	public static Economy economy = null;
	public static Plugin vault = null;

	public static int useCost = 0;
	public static int createCost = 0;
	public static int destroyCost = 0;
	public static boolean toOwner = false;
	public static boolean chargeFreeDestination = true;
	public static boolean freeGatesGreen = false;

	public static double getBalance(String player) {
		if (!economyEnabled) return 0;
		return economy.getBalance(player);
	}

	public static boolean chargePlayer(String player, String target, double amount) {
		if (!economyEnabled) return true;
		if(player.equals(target)) return true;
		if(economy != null) {
			if(!economy.has(player, amount)) return false;
			economy.withdrawPlayer(player, amount);
			if(target != null) economy.depositPlayer(target, amount);
		}
		return false;
	}

	public static String format(int amt) {
		if (economyEnabled) {
			return economy.format(amt);
		}
		return "";
	}

	public static boolean setupEconomy(PluginManager pm) {
		if (!economyEnabled) return false;
		// Check for Vault
		Plugin p = pm.getPlugin("Vault");
		if (p != null && p.isEnabled()) {
			RegisteredServiceProvider<Economy> economyProvider = Stargate.server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (economyProvider != null) {
				economy = economyProvider.getProvider();
				vault = p;
				return true;
			}
		}
		economyEnabled = false;
		return false;
	}

	public static boolean useEconomy() {
		if(!economyEnabled || economy == null) return false;
		return true;
	}

}