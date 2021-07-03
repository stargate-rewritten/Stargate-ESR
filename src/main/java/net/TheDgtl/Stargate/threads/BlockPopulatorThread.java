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
	
	static private final long ENDGATEWAY_AGE = -999999999; //a negative time removes the purple beams

	
    public BlockPopulatorThread(@NotNull Stargate stargate) {
        super(stargate);
    }
    
    
    public void run() {
        long sTime = System.nanoTime();

        while (System.nanoTime() - sTime < 25000000) {
            BloxPopulator b = stargate.getBlockPopulatorQueue().poll();
            if (b == null) return;

			Block blk = b.getBlox().getBlock();
			Material mat = b.getMat();
            blk.setType(mat, false);
			if (mat == Material.END_GATEWAY) {
				// force a location to prevent exit gateway generations
				EndGateway gateway = (EndGateway) blk.getState();
				if (blk.getWorld().getEnvironment() == World.Environment.THE_END) {
					gateway.setExitLocation(blk.getWorld().getSpawnLocation());
					gateway.setExactTeleport(true);
				}
				gateway.setAge(ENDGATEWAY_AGE);
				gateway.update(false, false);
				
				continue;
			}
            if (b.getAxis() != null) {
                Orientable orientable = (Orientable) blk.getBlockData();
                orientable.setAxis(b.getAxis());
                blk.setBlockData(orientable);
                continue;
            }
        }
    }

}
