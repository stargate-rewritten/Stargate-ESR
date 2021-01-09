package net.TheDgtl.Stargate.threads;

import java.util.Iterator;
import net.TheDgtl.Stargate.Portal;
import net.TheDgtl.Stargate.Stargate;
import org.jetbrains.annotations.NotNull;

public class SGThread extends StargateRunnable {

    public SGThread(@NotNull Stargate stargate) {
        super(stargate);
    }

    public void run() {
        long time = System.currentTimeMillis() / 1000;

        // Close open portals



        for (Iterator<Portal> iter = stargate.getOpenList().iterator(); iter.hasNext(); ) {
            Portal p = iter.next();

            if (p.isAlwaysOn()) continue; // Skip always open gates
            if (!p.isOpen()) continue;

            if (time > p.getOpenTime() + stargate.getOpenTime()) {
                p.close(false);
                iter.remove();
            }
        }

        // Deactivate active portals
        for (Iterator<Portal> iter = stargate.getActiveList().iterator(); iter.hasNext(); ) {
            Portal p = iter.next();

            if (!p.isActive()) continue;

            if (time > p.getOpenTime() + stargate.getActiveTime()) {
                p.deactivate();
                iter.remove();
            }
        }
    }

}
