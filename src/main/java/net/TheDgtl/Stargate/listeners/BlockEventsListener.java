package net.TheDgtl.Stargate.listeners;

import net.TheDgtl.Stargate.EconomyHandler;
import net.TheDgtl.Stargate.Portal;
import net.TheDgtl.Stargate.Stargate;
import net.TheDgtl.Stargate.event.StargateDestroyEvent;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.SignChangeEvent;

public class BlockEventsListener implements Listener {
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (!(block.getBlockData() instanceof WallSign)) return;

        final Portal portal = Portal.createPortal(event, player);
        // Not creating a gate, just placing a sign
        if (portal == null) return;

        Stargate.sendMessage(player, Stargate.getString("createMsg"), false);
        Stargate.debug("onSignChange", "Initialized stargate: " + portal.getName());
        Stargate.server.getScheduler().scheduleSyncDelayedTask(Stargate.stargate, new Runnable() {
            public void run() {
                portal.drawSign();
            }
        }, 1);
    }

    // Switch to HIGHEST priority so as to come after block protection plugins (Hopefully)
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        Player player = event.getPlayer();
        Portal portal = Portal.getByBlock(block);

        if (portal == null && Stargate.protectEntrance)
            portal = Portal.getByEntrance(block);
        if (portal == null) return;

        boolean deny = false;
        String denyMsg = "";

        if (!Stargate.canDestroy(player, portal)) {
            denyMsg = "Permission Denied"; // TODO: Change to Stargate.getString()
            deny = true;
            Stargate.log.info("[Stargate] " + player.getName() + " tried to destroy gate");
        }

        int cost = Stargate.getDestroyCost(player, portal.getGate());

        StargateDestroyEvent dEvent = new StargateDestroyEvent(portal, player, deny, denyMsg, cost);
        Stargate.server.getPluginManager().callEvent(dEvent);

        boolean denied = dEvent.getDeny();
        boolean cancelled = denied || dEvent.isCancelled();

        if (cancelled) {
            if (denied) {
                Stargate.sendMessage(player, dEvent.getDenyReason());
            }

            event.setCancelled(true);
            return;
        }

        cost = dEvent.getCost();

        if (cost != 0) {
            if (!Stargate.chargePlayer(player, cost)) {
                Stargate.debug("onBlockBreak", "Insufficient Funds");
                Stargate.sendMessage(player, Stargate.getString("inFunds"));

                event.setCancelled(true);
                return;
            }

            boolean deduct = cost > 0;
            int fCost = deduct ? cost : -cost;

            String msg = deduct ? Stargate.getString("ecoDeduct") : Stargate.getString("ecoRefund");
            msg = Stargate.replaceVars(msg, new String[]{"%cost%", "%portal%"}, new String[]{EconomyHandler.format(fCost), portal.getName()});

            Stargate.sendMessage(player, msg, false);
        }

        portal.unregister(true);
        Stargate.sendMessage(player, Stargate.getString("destroyMsg"), false);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        Portal portal = null;

        // Handle keeping portal material and buttons around
        switch (block.getType()) {
            case NETHER_PORTAL:
                portal = Portal.getByEntrance(block);
                break;
            case STONE_BUTTON:
            case DEAD_TUBE_CORAL_WALL_FAN:
                portal = Portal.getByControl(block);
                break;
        }

        if (portal != null) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Portal portal = Portal.getByEntrance(event.getBlock());

        if (portal == null) {
            return;
        }

        boolean cancelled = event.getBlock().getY() == event.getToBlock().getY();
        event.setCancelled(cancelled);
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            Portal portal = Portal.getByBlock(block);

            if (portal != null) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (!event.isSticky()) return;

        for (Block block : event.getBlocks()) {
            Portal portal = Portal.getByBlock(block);

            if (portal != null) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
