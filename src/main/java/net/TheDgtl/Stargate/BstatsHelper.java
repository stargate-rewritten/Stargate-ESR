/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.TheDgtl.Stargate;

import org.bukkit.plugin.java.JavaPlugin;

import org.bstats.bukkit.Metrics;

/**
 *  A helper for dealing with metrics
 *  @author Knarvik, Thorin
 */
public class BstatsHelper {

    private static boolean hasBeenInitialized = false;

    private BstatsHelper() {

    }

    /**
     * Initializes Bstats
     */

    public static void initialize(JavaPlugin plugin) {
        if (hasBeenInitialized) {
            throw new IllegalArgumentException("Bstats initialized twice");
        } else {
            hasBeenInitialized = true;
        }

        int pluginId = 10451;
        Metrics metrics = new Metrics(plugin, pluginId);
    }
}
