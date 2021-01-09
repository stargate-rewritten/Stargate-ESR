package net.TheDgtl.Stargate.listeners;

import java.util.List;
import net.TheDgtl.Stargate.Portal;
import net.TheDgtl.Stargate.Stargate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class MovementEventsListener implements Listener {
    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        if (!Stargate.handleVehicles) return;
        List<Entity> passengers = event.getVehicle().getPassengers();
        Vehicle vehicle = event.getVehicle();

        Portal portal = Portal.getByEntrance(event.getTo());
        if (portal == null || !portal.isOpen()) return;

        // We don't support vehicles in Bungee portals
        if (portal.isBungee()) return;

        if (!passengers.isEmpty() && passengers.get(0) instanceof Player) {
            /*
                            Player player = (Player) passengers.get(0);
            if (!portal.isOpenFor(player)) {
                Stargate.sendMessage(player, Stargate.getString("denyMsg"));
                return;
            }

            Portal dest = portal.getDestination(player);
            if (dest == null) return;
            boolean deny = false;
            // Check if player has access to this network
            if (!canAccessNetwork(player, portal.getNetwork())) {
                deny = true;
            }

            // Check if player has access to destination world
            if (!canAccessWorld(player, dest.getWorld().getName())) {
                deny = true;
            }

            if (!canAccessPortal(player, portal, deny)) {
                Stargate.sendMessage(player, Stargate.getString("denyMsg"));
                portal.close(false);
                return;
            }

            int cost = Stargate.getUseCost(player, portal, dest);
            if (cost > 0) {
                boolean success;
                if(portal.getGate().getToOwner()) {
                    if(portal.getOwnerUUID() == null) {
                        success = Stargate.chargePlayer(player, portal.getOwnerUUID(), cost);
                    } else {
                        success = Stargate.chargePlayer(player, portal.getOwnerName(), cost);
                    }
                } else {
                    success = Stargate.chargePlayer(player, cost);
                }
                if(!success) {
                    // Insufficient Funds
                    Stargate.sendMessage(player, Stargate.getString("inFunds"));
                    portal.close(false);
                    return;
                }
                String deductMsg = Stargate.getString("ecoDeduct");
                deductMsg = Stargate.replaceVars(deductMsg, new String[] {"%cost%", "%portal%"}, new String[] {EconomyHandler.format(cost), portal.getName()});
                sendMessage(player, deductMsg, false);
                if (portal.getGate().getToOwner()) {
                    Player p;
                    if(portal.getOwnerUUID() != null) {
                        p = server.getPlayer(portal.getOwnerUUID());
                    } else {
                        p = server.getPlayer(portal.getOwnerName());
                    }
                    if (p != null) {
                        String obtainedMsg = Stargate.getString("ecoObtain");
                        obtainedMsg = Stargate.replaceVars(obtainedMsg, new String[] {"%cost%", "%portal%"}, new String[] {EconomyHandler.format(cost), portal.getName()});
                        Stargate.sendMessage(p, obtainedMsg, false);
                    }
                }
            }

            Stargate.sendMessage(player, Stargate.getString("teleportMsg"), false);
            dest.teleport(vehicle);
            portal.close(false);
                            */
        } else {
            Portal dest = portal.getDestination();
            if (dest == null) return;
            dest.teleport(vehicle);
        }
    }
}
