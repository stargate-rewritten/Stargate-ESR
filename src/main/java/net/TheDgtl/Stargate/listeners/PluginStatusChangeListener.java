package net.TheDgtl.Stargate.listeners;

import net.TheDgtl.Stargate.EconomyHandler;
import net.TheDgtl.Stargate.Stargate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginStatusChangeListener implements Listener {

    private final Stargate stargate;

    public PluginStatusChangeListener(Stargate stargate) {
        this.stargate = stargate;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (EconomyHandler.setupEconomy(stargate.getServer().getPluginManager())) {
            Stargate.log.info("[Stargate] Vault v" + EconomyHandler.vault.getDescription().getVersion() + " found");
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(EconomyHandler.vault)) {
            Stargate.log.info("[Stargate] Vault plugin lost.");
        }
    }

}
