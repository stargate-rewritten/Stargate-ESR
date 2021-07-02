package net.TheDgtl.Stargate.threads;

import net.TheDgtl.Stargate.BloxPopulator;
import net.TheDgtl.Stargate.Stargate;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;
import org.bukkit.block.data.Orientable;
import org.jetbrains.annotations.NotNull;

public class BlockPopulatorThread extends StargateRunnable {

    public BlockPopulatorThread(@NotNull Stargate stargate) {
        super(stargate);
    }

    public void run() {
        long sTime = System.nanoTime();

        while (System.nanoTime() - sTime < 25000000) {
            BloxPopulator b = stargate.getBlockPopulatorQueue().poll();
            if (b == null) return;

            Block blk = b.getBlox().getBlock();
            blk.setType(b.getMat(), false);

            if (b.getMat() == Material.END_GATEWAY && blk.getWorld().getEnvironment() == World.Environment.THE_END) {
                // force a location to prevent exit gateway generation
                EndGateway gateway = (EndGateway) blk.getState();
                gateway.setExitLocation(blk.getWorld().getSpawnLocation());
                gateway.setExactTeleport(true);
                // https://github.com/stargate-bukkit/Stargate-Bukkit/issues/36
                gateway.setAge(-9223372036854775808L);
                gateway.update(false, false);
            } else if (b.getAxis() != null) {
                Orientable orientable = (Orientable) blk.getBlockData();
                orientable.setAxis(b.getAxis());
                blk.setBlockData(orientable);
            }
        }
    }

}
