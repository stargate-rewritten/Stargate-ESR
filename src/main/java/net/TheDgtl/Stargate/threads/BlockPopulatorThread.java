package net.TheDgtl.Stargate.threads;

import net.TheDgtl.Stargate.BloxPopulator;
import net.TheDgtl.Stargate.NonLegacyMethod;
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

        while (System.nanoTime() - sTime < 50000000) {
            BloxPopulator b = stargate.getBlockPopulatorQueue().poll();
            if (b == null) return;
            
            b.getBlox().getBlock().setType(b.getMat(), false);
            b.getBlox().getBlock().setData(b.getData(), false);
            
            Block blk = b.getBlox().getBlock();
            blk.setType(b.getMat(), false);
            
            if (b.getMat().toString() == "END_GATEWAY") {
                // force a location to prevent exit gateway generation
                EndGateway gateway = (EndGateway) blk.getState();
                // https://github.com/stargate-bukkit/Stargate-Bukkit/issues/36
                NonLegacyMethod.END_GATEWAY.invoke(blk, -9223372036854775808L);
                if(blk.getWorld().getEnvironment() == World.Environment.THE_END){
                      gateway.setExitLocation(blk.getWorld().getSpawnLocation());
                      gateway.setExactTeleport(true);
                }
                  gateway.update(false, false);
            }
        }
    }

}
