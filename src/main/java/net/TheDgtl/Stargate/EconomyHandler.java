package net.TheDgtl.Stargate;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

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
 */

public class EconomyHandler {

    private Stargate plugin;

    public EconomyHandler(Stargate plugin) {
        this.plugin = plugin;
    }

	public boolean economyEnabled = false;
	public Economy economy = null;
	public Plugin vault = null;

	public int useCost = 0;
	public int createCost = 0;
	public int destroyCost = 0;
	public boolean toOwner = false;
	public boolean chargeFreeDestination = true;
	public boolean freeGatesGreen = false;

	public double getBalance(Player player) {
		if (!economyEnabled) return 0;
		return economy.getBalance(player);
	}

	public boolean chargePlayer(Player player, String target, double amount) {
		if (!economyEnabled) return true;
		if(player.getName().equals(target)) return true;
		if(economy != null) {
			if(!economy.has(player, amount)) return false;
			economy.withdrawPlayer(player, amount);
			economy.depositPlayer(target, amount);
		}
		return false;
	}

	public boolean chargePlayer(Player player, UUID target, double amount) {
		if (!economyEnabled) return true;
		if(player.getUniqueId().compareTo(target) == 0) return true;
		if(economy != null) {
			if(!economy.has(player, amount)) return false;
			economy.withdrawPlayer(player, amount);
			economy.depositPlayer(Bukkit.getOfflinePlayer(target), amount);
		}
		return false;
	}

	public boolean chargePlayer(Player player, double amount) {
		if (!economyEnabled) return true;
		if(economy != null) {
			if(!economy.has(player, amount)) return false;
			economy.withdrawPlayer(player, amount);
		}
		return false;
	}

	public String format(int amt) {
		if (economyEnabled) {
			return economy.format(amt);
		}
		return "";
	}

	public boolean setupEconomy(PluginManager pm) {
		if (!economyEnabled) return false;
		// Check for Vault
		Plugin p = pm.getPlugin("Vault");
		if (p != null && p.isEnabled()) {
			RegisteredServiceProvider<Economy> economyProvider = plugin.server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (economyProvider != null) {
				economy = economyProvider.getProvider();
				vault = p;
				return true;
			}
		}
		economyEnabled = false;
		return false;
	}

	public boolean useEconomy() {
		return economyEnabled && economy != null;
	}

}
