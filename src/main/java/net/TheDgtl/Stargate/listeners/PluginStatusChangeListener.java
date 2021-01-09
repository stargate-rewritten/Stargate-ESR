package net.TheDgtl.Stargate.listeners;

import java.util.Objects;
import net.TheDgtl.Stargate.EconomyHandler;
import net.TheDgtl.Stargate.Stargate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.jetbrains.annotations.NotNull;

public class PluginStatusChangeListener extends StargateListener {

    public PluginStatusChangeListener(@NotNull Stargate stargate) {
        super(stargate);
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        EconomyHandler economyHandler = stargate.getEconomyHandler();
        if (economyHandler.setupEconomy(stargate.getServer().getPluginManager())) {
            stargate.getStargateLogger().info("[Stargate] Vault v" + Objects.requireNonNull(economyHandler.getVault()).getDescription().getVersion() + " found");
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(stargate.getEconomyHandler().getVault())) {
            stargate.getStargateLogger().info("[Stargate] Vault plugin lost.");
        }
    }

}
