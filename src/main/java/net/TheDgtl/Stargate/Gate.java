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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

public class Gate {

    private static final Character ANYTHING = ' ';
    private static final Character ENTRANCE = '.';
    private static final Character EXIT = '*';

    private static final HashMap<String, Gate> gates = new HashMap<>();
    private static final HashMap<Material, ArrayList<Gate>> controlBlocks = new HashMap<>();
    private static final HashSet<Material> frameBlocks = new HashSet<>();

    private final String filename;
    private final Character[][] layout;
    private final HashMap<Character, Material> types;
    private RelativeBlockVector[] entrances = new RelativeBlockVector[0];
    private RelativeBlockVector[] border = new RelativeBlockVector[0];
    private RelativeBlockVector[] controls = new RelativeBlockVector[0];
    private RelativeBlockVector exitBlock = null;
    private final HashMap<RelativeBlockVector, Integer> exits = new HashMap<>();
    private Material portalBlockOpen = Material.PORTAL;
    private Material portalBlockClosed = Material.AIR;

    // Economy information
    private int useCost = -1;
    private int createCost = -1;
    private int destroyCost = -1;
    private boolean toOwner = false;

    // di
    private final Stargate stargate;

    public Gate(Stargate stargate, String filename, Character[][] layout, HashMap<Character, Material> types) {
        this.stargate = stargate;
        this.filename = filename;
        this.layout = layout;
        this.types = types;

        populateCoordinates();
    }

    private void populateCoordinates() {
        ArrayList<RelativeBlockVector> entranceList = new ArrayList<>();
        ArrayList<RelativeBlockVector> borderList = new ArrayList<>();
        ArrayList<RelativeBlockVector> controlList = new ArrayList<>();
        RelativeBlockVector[] relativeExits = new RelativeBlockVector[layout[0].length];
        int[] exitDepths = new int[layout[0].length];
        RelativeBlockVector lastExit = null;

        for (int y = 0; y < layout.length; y++) {
            for (int x = 0; x < layout[y].length; x++) {
                Character key = layout[y][x];

                if (key.equals('-')) {
                    controlList.add(new RelativeBlockVector(x, y, 0));
                }

                if (key.equals(ANYTHING)) continue;

                if (key.equals(ENTRANCE) || key.equals(EXIT)) {
                    entranceList.add(new RelativeBlockVector(x, y, 0));
                    exitDepths[x] = y;

                    if (key.equals(EXIT)) {
                        this.exitBlock = new RelativeBlockVector(x, y, 0);
                    }

                    continue;
                }

                borderList.add(new RelativeBlockVector(x, y, 0));
            }
        }

        for (int x = 0; x < exitDepths.length; x++) {
            relativeExits[x] = new RelativeBlockVector(x, exitDepths[x], 0);
        }

        for (int x = relativeExits.length - 1; x >= 0; x--) {
            if (relativeExits[x] != null) {
                lastExit = relativeExits[x];
            } else {
                relativeExits[x] = lastExit;
            }

            if (exitDepths[x] > 0)
                this.exits.put(relativeExits[x], x);
        }

        this.entrances = entranceList.toArray(this.entrances);
        this.border = borderList.toArray(this.border);
        this.controls = controlList.toArray(this.controls);
    }

    public Character[][] getLayout() {
        return layout;
    }

    public HashMap<Character, Material> getTypes() {
        return types;
    }

    public RelativeBlockVector[] getEntrances() {
        return entrances;
    }

    public RelativeBlockVector[] getBorder() {
        return border;
    }

    public RelativeBlockVector[] getControls() {
        return controls;
    }

    public HashMap<RelativeBlockVector, Integer> getExits() {
        return exits;
    }

    public boolean isValidControllBlock(Material block) {
	return (getControlBlock() != null) && getControlBlock().equals(block);
    }
        
    public RelativeBlockVector getExit() {
        return exitBlock;
    }

    public Material getControlBlock() {
        return types.get('-');
    }

    public String getFilename() {
        return filename;
    }

    public Material getPortalBlockOpen() {
        return portalBlockOpen;
    }

    public void setPortalBlockOpen(Material type) {
        portalBlockOpen = type;
    }

    public Material getPortalBlockClosed() {
        return portalBlockClosed;
    }

    public void setPortalBlockClosed(Material type) {
        portalBlockClosed = type;
    }

    public int getUseCost() {
        if (useCost < 0)
            return stargate.getEconomyHandler().getUseCost();
        return useCost;
    }

    public Integer getCreateCost() {
        if (createCost < 0)
            return stargate.getEconomyHandler().getCreateCost();
        return createCost;
    }

    public Integer getDestroyCost() {
        if (destroyCost < 0)
            return stargate.getEconomyHandler().getDestroyCost();
        return destroyCost;
    }

    public Boolean getToOwner() {
        return toOwner;
    }

    public boolean matches(Blox topleft, int modX, int modZ) {
        return matches(topleft, modX, modZ, false);
    }
    
    /**
     * Checks if this gate layout matches with its surrounding
     * 
     * @param topleft 
     * @param modX     identifier which gives the direction of the portal
     * @param modZ     identifier which gives the direction of the portal
     * @param onCreate
     * @return true if it matched
     */
    public boolean matches(Blox topleft, int modX, int modZ, boolean onCreate) {
        HashMap<Character, Material> portalTypes = new HashMap<>(types);

        for (int y = 0; y < layout.length; y++) {
            for (int x = 0; x < layout[y].length; x++) {
                Character key = layout[y][x];

                if (key.equals(ANYTHING)) {
                    continue;
                }

                if (key.equals(ENTRANCE) || key.equals(EXIT)) {
                    if (stargate.isIgnoreEntrance())
                        continue;

                    Material type = topleft.modRelative(x, y, 0, modX, 1, modZ).getType();
                    

                    if (type != portalBlockClosed && type != portalBlockOpen) {
                        stargate.debug("Gate::Matches", "Entrance/Exit Material Mismatch: " + type);
                        return false;
                    }

                    continue;
                }

                Material id = portalTypes.get(key);
                //boolean matches = false;
                if (id == null) {
                    portalTypes.put(key, topleft.modRelative(x, y, 0, modX, 1, modZ).getType());
                    continue;
                }

                Material blockType = topleft.modRelative(x, y, 0, modX, 1, modZ).getType();

                String idString = id.toString();
                String blockString = blockType.toString();

                boolean matches = blockType == id;

                // Hack 7/5/2020
                // using LEGACY_* as a wildcard
                // Using LEGACY_CONCRETE will match ALL concrete colours
                if (idString.contains("LEGACY") && !matches) {
                    String noLegacy = idString.replace("LEGACY_", "");
                    matches = blockString.contains(noLegacy);
                }

                if (!matches) {
                    stargate.debug("Gate::Matches", "Block Type Mismatch: "
                            + topleft.modRelative(x, y, 0, modX, 1, modZ).getType() + " != " + id);
                    return false;
                }
            }
        }

        return true;
    }

    public static void registerGate(Gate gate) {
        gates.put(gate.getFilename(), gate);

        Material blockID = gate.getControlBlock();
        
        if (blockID != null){
            if (!controlBlocks.containsKey(blockID)) {
                controlBlocks.put(blockID, new ArrayList<>());
            }
            controlBlocks.get(blockID).add(gate);
            return;
        }

        controlBlocks.get(blockID).add(gate);
    }

    public static Gate loadGate(Stargate stargate, File file) {
        Scanner scanner = null;
        boolean designing = false;
        ArrayList<ArrayList<Character>> design = new ArrayList<>();
        HashMap<Character, Material> types = new HashMap<>();
        HashMap<String, String> config = new HashMap<>();
        HashSet<Material> frameTypes = new HashSet<>();
        int cols = 0;

        // Init types map
        types.put(ENTRANCE, Material.AIR);
        types.put(EXIT, Material.AIR);
        types.put(ANYTHING, Material.AIR);

        try {
            scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (designing) {
                    ArrayList<Character> row = new ArrayList<>();

                    if (line.length() > cols) {
                        cols = line.length();
                    }

                    for (Character symbol : line.toCharArray()) {
                        if ((symbol.equals('?')) || (!types.containsKey(symbol))) {
                            stargate.getStargateLogger().log(Level.SEVERE, "Could not load Gate " + file.getName()
                                    + " - Unknown symbol '" + symbol + "' in diagram");
                            return null;
                        }

                        row.add(symbol);
                    }

                    design.add(row);
                    continue;
                }

                if ((line.isEmpty()) || (!line.contains("="))) {
                    designing = true;
                    continue;
                }

                String[] split = line.split("=");
                String key = split[0].trim();
                String value = split[1].trim();

                if (key.length() != 1) {
                    config.put(key, value);
                    continue;
                }

                Character symbol = key.charAt(0);
                Material id = Material.getMaterial(value);

                if (id == null) {
                    throw new Exception("Invalid material in line: " + line);
                }

                types.put(symbol, id);
                frameTypes.add(id);
            }
        } catch (Exception ex) {
            stargate.getStargateLogger().log(Level.SEVERE,
                    "Could not load Gate " + file.getName() + " - " + ex.getMessage());
            return null;
        } finally {
            if (scanner != null)
                scanner.close();
        }

        Character[][] layout = new Character[design.size()][cols];

        for (int y = 0; y < design.size(); y++) {
            ArrayList<Character> row = design.get(y);
            Character[] result = new Character[cols];

            int rowSize = row.size();

            for (int x = 0; x < cols; x++) {
                result[x] = (x < rowSize) ? row.get(x) : ' ';
            }

            layout[y] = result;
        }

        Gate gate = new Gate(stargate, file.getName(), layout, types);

        gate.portalBlockOpen = readConfig(stargate, config, file, "portal-open", gate.portalBlockOpen);
        gate.portalBlockClosed = readConfig(stargate, config, file, "portal-closed", gate.portalBlockClosed);
        gate.useCost = readConfig(stargate, config, file, "usecost", -1);
        gate.destroyCost = readConfig(stargate, config, file, "destroycost", -1);
        gate.createCost = readConfig(stargate, config, file, "createcost", -1);
        gate.toOwner = (config.containsKey("toowner") ? Boolean.parseBoolean(config.get("toowner"))
                : stargate.getEconomyHandler().isToOwner()) || JavaPlugin.getPlugin(Stargate.class).getConfig().getBoolean("toowner");

        if (gate.getControls().length != 2) {
            stargate.getStargateLogger().log(Level.SEVERE,
                    "Could not load Gate " + file.getName() + " - Gates must have exactly 2 control points.");
            return null;
        }

        // Merge frame types, add open mat to list
        frameBlocks.addAll(frameTypes);

        //gate.save(file.getParent() + "/"); // Updates format for version changes
        return gate;
    }

    private static int readConfig(Stargate stargate, HashMap<String, String> config, File file, String key, int def) {
        if (config.containsKey(key)) {
            try {
                return Integer.parseInt(config.get(key));
            } catch (NumberFormatException ex) {
                stargate.getStargateLogger().log(Level.WARNING,
                        String.format("%s reading %s: %s is not numeric", ex.getClass().getName(), file, key));
            }
        }

        return def;
    }

    private static Material readConfig(Stargate stargate, HashMap<String, String> config, File file, String key, Material def) {
        if (config.containsKey(key)) {
            Material mat = Material.getMaterial(config.get(key));
            if (mat != null) return mat;
            stargate.getStargateLogger().log(Level.WARNING, String.format("Error reading %s: %s is not a material", file, key));
        }

        return def;
    }

    public static void loadGates(Stargate stargate, String gateFolder) {
        File dir = new File(gateFolder);
        File[] files = dir.exists() ? dir.listFiles(new StargateFilenameFilter()) : new File[0];

        for (File file : files) {
            Gate gate = loadGate(stargate, file);
            if (gate != null)
                registerGate(gate);
        }
    }

    public static Gate[] getGatesByControlBlock(Block block) {
        return getGatesByControlBlock(block.getType());
    }

    public static Gate[] getGatesByControlBlock(Material type) {
        Gate[] result = new Gate[0];
        ArrayList<Gate> lookup = controlBlocks.get(type);

        if (lookup != null) result = lookup.toArray(result);

        return result;
    }

    public static Gate getGateByName(String name) {
        return gates.get(name);
    }

    public static int getGateCount() {
        return gates.size();
    }

    public static boolean isGateBlock(Material type) {
        return frameBlocks.contains(type);
    }

    static class StargateFilenameFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return name.endsWith(".gate");
        }
    }

    public static void clearGates() {
        gates.clear();
        controlBlocks.clear();
        frameBlocks.clear();
    }

}