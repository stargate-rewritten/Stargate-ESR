package net.TheDgtl.Stargate.listeners;

import java.util.List;
import net.TheDgtl.Stargate.Portal;
import net.TheDgtl.Stargate.Stargate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.jetbrains.annotations.NotNull;

public class MovementEventsListener extends StargateListener {

    public MovementEventsListener(@NotNull Stargate stargate) {
        super(stargate);
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        if (!stargate.isHandleVehicles()) return;
        List<Entity> passengers = event.getVehicle().getPassengers();
        Vehicle vehicle = event.getVehicle();

        Portal portal = Portal.getByEntrance(event.getTo());
        if (portal == null || !portal.isOpen()) return;

        // We don't support vehicles in Bungee portals
        if (portal.isBungee()) return;

        //noinspection StatementWithEmptyBody
        if (!passengers.isEmpty() && passengers.get(0) instanceof Player) {
        } else {
            Portal dest = portal.getDestination();
            if (dest == null) return;
            dest.teleport(vehicle);
        }
    }
}
