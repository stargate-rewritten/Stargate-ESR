package net.TheDgtl.Stargate.threads;

import java.util.Iterator;
import net.TheDgtl.Stargate.Portal;
import net.TheDgtl.Stargate.Stargate;

public class SGThread implements Runnable {

    public void run() {
        long time = System.currentTimeMillis() / 1000;

        // Close open portals
        for (Iterator<Portal> iter = Stargate.openList.iterator(); iter.hasNext(); ) {
            Portal p = iter.next();

            if (p.isAlwaysOn()) continue; // Skip always open gates
            if (!p.isOpen()) continue;

            if (time > p.getOpenTime() + Stargate.openTime) {
                p.close(false);
                iter.remove();
            }
        }

        // Deactivate active portals
        for (Iterator<Portal> iter = Stargate.activeList.iterator(); iter.hasNext(); ) {
            Portal p = iter.next();

            if (!p.isActive()) continue;

            if (time > p.getOpenTime() + Stargate.activeTime) {
                p.deactivate();
                iter.remove();
            }
        }
    }

}
