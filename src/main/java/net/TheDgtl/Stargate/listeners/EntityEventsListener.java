package net.TheDgtl.Stargate.listeners;

import net.TheDgtl.Stargate.Portal;
import net.TheDgtl.Stargate.Stargate;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

public class EntityEventsListener extends StargateListener {

    public EntityEventsListener(@NotNull Stargate stargate) {
        super(stargate);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) return;

        for (Block b : event.blockList()) {
            Portal portal = Portal.getByBlock(b);
            if (portal == null) continue;

            if (!stargate.isDestroyExplosion()) {
                event.setCancelled(true);
                break;
            }

            portal.unregister(true);
        }
    }

}
