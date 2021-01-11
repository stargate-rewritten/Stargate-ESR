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

import org.bukkit.Axis;
import org.bukkit.Material;

public class BloxPopulator {
    private Blox blox;
    private Material nextMat;
    private Axis nextAxis;

    public BloxPopulator(Blox b, Material m) {
        blox = b;
        nextMat = m;
        nextAxis = null;
    }

    public BloxPopulator(Blox b, Material m, Axis a) {
        blox = b;
        nextMat = m;
        nextAxis = a;
    }

    public void setBlox(Blox b) {
        blox = b;
    }

    public void setMat(Material m) {
        nextMat = m;
    }

    public void setAxis(Axis a) {
        nextAxis = a;
    }

    public Blox getBlox() {
        return blox;
    }

    public Material getMat() {
        return nextMat;
    }

    public Axis getAxis() {
        return nextAxis;
    }

}
