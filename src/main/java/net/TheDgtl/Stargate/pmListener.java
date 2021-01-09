/*
 * Stargate - A portal plugin for Bukkit
 * Copyright (C) 2011, 2012 Steven "Drakia" Scott <Contact@TheDgtl.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.TheDgtl.Stargate;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class pmListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player unused, byte[] message) {
        if (!Stargate.enableBungee || !channel.equals("BungeeCord")) return;

        // Get data from message
        String inChannel;
        byte[] data;
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            inChannel = in.readUTF();
            short len = in.readShort();
            data = new byte[len];
            in.readFully(data);
        } catch (IOException ex) {
            Stargate.log.severe("[Stargate] Error receiving BungeeCord message");
            ex.printStackTrace();
            return;
        }

        // Verify that it's an SGBungee packet
        if (!inChannel.equals("SGBungee")) {
            return;
        }

        // Data should be player name, and destination gate name
        String msg = new String(data);
        String[] parts = msg.split("#@#");

        String playerName = parts[0];
        String destination = parts[1];

        // Check if the player is online, if so, teleport, otherwise, queue
        Player player = Stargate.server.getPlayer(playerName);
        if (player == null) {
            Stargate.bungeeQueue.put(playerName.toLowerCase(), destination);
        } else {
            Portal dest = Portal.getBungeeGate(destination);
            // Specified an invalid gate. For now we'll just let them connect at their current location
            if (dest == null) {
                Stargate.log.info("[Stargate] Bungee gate " + destination + " does not exist");
                return;
            }
            dest.teleport(player, dest, null);
        }
    }
}
