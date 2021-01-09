package net.TheDgtl.Stargate.listeners;

import net.TheDgtl.Stargate.Portal;
import net.TheDgtl.Stargate.Stargate;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityEventsListener implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) return;

        for (Block b : event.blockList()) {
            Portal portal = Portal.getByBlock(b);
            if (portal == null) continue;

            if (!Stargate.destroyExplosion) {
                event.setCancelled(true);
                break;
            }

            portal.unregister(true);
        }
    }

}
