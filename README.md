> **Note that improved documentation can now be found** [here](https://sgrewritten.org/wiki)<br>
> **Note that a** [support discord](https://sgrewritten.org/discord) **has now been created!**

> **THIS IS AN [EXTENDED SUPPORT RELEASE](https://sgrewritten.org/esr) FOR VERSIONS 1.13.2-1.16.5!**<br>
> **This version extends Drakia's original 2013 codebase, with some backported fixes**.<br>
> **THIS VERSION WILL RECEIVE NO SUPPORT BEYOND CRITICAL BUG FIXES**<br><br>
> **For modern versions of stargate, please look [here](https://sgrewritten.org/downloads) instead!**.

# Description
Create gates that allow for instant-teleportation between large distances. Gates can be always-open or triggered; they can share a network or be split into clusters; they can be hidden on a network or accessible to everybody.

- Player permissions -- let players build their own networks.
- Vault economy support -- can add costs for create, destroy and use.
- Capacity to add custom gates, as well as some pre-set examples
- Message customization
- Gate options
- Underwater portal support

## Background
- This plugin was originally TheDgtl's Bukkit port of the Stargate plugin for hMod by Dinnerbone.
- After this plugin was dropped by TheDgtl, PseudoKnight began maintaining it for modern versions of Spigot.
- LockedCraft forked that to fix some issues in the PseudoKnight version, update to 1.15, and add underwater portals.
- LittleBigBug forked that to clean up the project; meanwhile, PseudoKnight made some upstream changes.
- Version 10 a is fork (with LBB & PK's changes merged) made to update to 1.16 and resolve some underwater issues.
- This is an EXTENDED SUPPORT VERSION, backported to 1.13.2-1.16.5 and minimally maintained by [SGR](https://sgrewritten.org).

# Permissions
```
stargate.use -- Allow use of all gates linking to any world in any network (Override ALL network/world permissions. Set to false to use network/world specific permissions)  
  stargate.world -- Allow use of gates linking to any world  
    stargate.world.{world} -- Allow use of gates with a destination in {world}. Set to false to disallow use.  
  stargate.network -- Allow use of gates on all networks  
    stargate.network.{network} -- Allow use of all gates in {network}. Set to false to disallow use.  

stargate.option -- Allow use of all options
  stargate.option.hidden -- Allow use of 'H'idden
  stargate.option.alwayson -- Allow use of 'A'lways-On
  stargate.option.private -- Allow use of 'P'rivate
  stargate.option.free -- Allow use of 'F'ree
  stargate.option.backwards -- Allow use of 'B'ackwards
  stargate.option.show -- Allow use of 'S'how
  stargate.option.nonetwork -- Allow use of 'N'oNetwork
  stargate.option.random -- Allow use of 'Random' gates
  
stargate.create -- Allow creating gates on any network (Override all create permissions)
  stargate.create.personal -- Allow creating gates on network {playername}
  stargate.create.network -- Allow creating gates on any network
    stargate.create.network.{networkname} -- Allow creating gates on network {networkname}. Set to false to disallow creation on {networkname}
  stargate.create.gate -- Allow creation of any gate layout
    stargate.create.gate.{gatefile} -- Allow creation of only {gatefile} gates

stargate.destroy -- Allow destruction gates on any network (Orderride all destroy permissions)
  stargate.destroy.personal -- Allow destruction of gates owned by user only
  stargate.destroy.network -- Allow destruction of gates on any network
    stargate.destroy.network.{networkname} -- Allow destruction of gates on network {networkname}. Set to false to disallow destruction of {networkname}

stargate.free -- Allow free use/creation/destruction of gates
  stargate.free.use -- Allow free use of Stargates
  stargate.free.create -- Allow free creation of Stargates
  stargate.free.destroy -- Allow free destruction of Stargates
  
stargate.admin -- Allow all admin features (Hidden/Private only so far)
  stargate.admin.private -- Allow use of Private gates not owned by user
  stargate.admin.hidden -- Allow access to Hidden gates not ownerd by user
  stargate.admin.reload -- Allow use of /sg reload
```
## Default Permissions
```
stargate.use -- Everyone
stargate.create -- Op
stargate.destroy -- Op
stargate.option -- Op
stargate.free -- Op
stargate.admin -- Op
```

# Instructions
## Building a gate:
This is the default gate configuration. See the Custom Gate Layout section on how to change this.
```
    OO 
   O  O - These are Obsidian blocks (use Sea Lanterns if underwater). You need 10.
   ■  ■ - Place a sign on either of these two blocks.
   O  O
    OO
```

### Sign Layout:

- Line 1: Gate Name (Max 12 characters)
- Line 2: Destination Name [Optional] (Max 12 characters, used for fixed-gates only)
- Line 3: Network name [Optional] (Max 12 characters)
- Line 4: Options [Optional] :
  - 'A' for Always-on fixed gate
  - 'H' for Hidden networked gate
  - 'P' for a Private gate
  - 'F' for a Free gate
  - 'B' is for a Backwards facing gate (which exit you at the back)
  - 'S' is for Showing an always-on gate in the network list
  - 'N' is for hiding the Network name
  - 'R' is for always-on detached (Random) gates.

The options are the single letter, not the word. So to make a private hidden gate, your 4th line would be 'PH'.

#### Gate networks:
 - Gates are all part of a network, by default this is "central".
 - You can specify (and create) your own network on the third line of the sign when making a new gate.
 - Gates on one network will not see gates on the second network, and vice versa.
 - Gates on different worlds, but in the same network, will see eachother.

#### Fixed gates:
 - Fixed gates go to only one set destination.
 - Fixed gates can be linked to other fixed gates, or normal gates. A normal gate cannot open a portal to a fixed gate however.
 - To create a fixed gate, specify a destination on the second line of the stargate sign.
 - Set the 4th line of the stargate sign to "A" to enable an always-open fixed gate.
 
#### Hidden Gates:
 - Hidden gates are like normal gates, but only show on the destination list of other gates under certain conditions.
 - A hidden gate is only visible to the creator of the gate, or somebody with the stargate.hidden permission.
 - Set the 4th line of the stargate sign to 'H' to make it a hidden gate.

#### Detached (Random) Gates:
 - Always-on detached gates are similar to always-on fixed gates, but do not have a fixed exit;
   - They instead randomly select an exit from the list of gates on their network.
 - Marking a gate as 'R' will automatically make that gate always-on.
 - 'R' gates ignore any gate with the 'R', 'A', and/or 'S' flag(s) when choosing their exit.

## Using a gate:
 - Right click the sign to choose a destination (not needed for Fixed gates, undefined gates).
 - Right click the activator to open up a portal.
   - Normally, the activator is a button
   - If the gate is underwater, the activator will be a dead coral fan
 - Step through.
 
## Economy Support:
The latest version of Stargate has support for Vault. Gate creation, destruction and use can all have different costs associated with them. You can also define per-gate layout costs. The default cost is assigned in the config.yml file, while the per-gate costs re defined in the .gate files. To define a certain cost to a gate just add these lines to your .gate file:
```
usecost=5
destroycost=5
createcost=5
toowner=true
```

# Custom Gate Layout
Note that MATERIAL NAMES (such as `OBSIDIAN`) can be found [here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html).<br>
As of version 10.7, TAGS (such as `#WOOL`) can be found [here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Tag.html).
## Normal Portals
You can create as many gate formats as you want, the gate layouts are stored in plugins/Stargate/gates/.  
The .gate file must be laid out a specific way, the first lines will be config information, and after a blank line you will lay out the gate format. Here is the default nether.gate file:
```
portal-open=NETHER_PORTAL
portal-closed=AIR
X=OBSIDIAN
-=OBSIDIAN

 XX 
X..X
-..-
X*.X
 XX 
```
portal-open/closed are used to define the material in the gate when it is open or closed.   
"X" and "-" are used to define block "types" for the layout (Any single-character can be used, such as "#").  
In the gate format, you can see we use "X" to show where obsidian must be, "-" where the controls (Button/sign) are.  
You will also notice a "*" in the gate layout, this is the "exit point" of the gate, the block at which the player will teleport in front of.

## Underwater Portals
By default, all portals do not function properly when waterlogged (built underwater).
To make an underwater gate, set the portal-closed value to WATER.

Note that this will replace the button with some coral; the functionality remains the same insofar as you must right click said coral.
```
portal-open=KELP_PLANT
portal-closed=WATER
X=SEA_LANTERN
-=SEA_LANTERN

 XX 
X..X
-..-
X*.X
 XX 
```

# Configuration
```
default-gate-network - The default gate network
portal-folder - The folder your portal databases are saved in
gate-folder - The folder containing your .gate files
destroyexplosion - Whether to destroy a stargate with explosions, or stop an explosion if it contains a gates controls.
useeconomy - Whether or not to use Economy
createcost - The cost to create a stargate
destroycost - The cost to destroy a stargate (Can be negative for a "refund"
usecost - The cost to use a stargate
chargefreedestination - Enable to allow free travel from any gate to a free gate
freegatesgreen - Enable to make gates that won't cost the player money show up as green
toowner - Whether the money from gate-use goes to the owner or nobody
maxgates - If non-zero, will define the maximum amount of gates allowed on any network.
lang - The language to use (Included languages: en, de)
destMemory - Whether to set the first destination as the last used destination for all gates
ignoreEntrance - Set this option to true to not check the entrance of a gate on startup. This is a workaround for snowmen breaking gates.
handleVehicles - Whether or not to handle vehicles going through gates. Set to false to disallow vehicles (Manned or not) going through gates.
sortLists - If true, network lists will be sorted alphabetically.
protectEntrance - If true, will protect from users breaking gate entrance blocks (This is more resource intensive than the usual check, and should only be enabled for servers that use solid open/close blocks)
signColor: This allows you to specify the color of the gate signs. Valid colors:
verifyPortals: Whether or not all the non-sign blocks are checked to match the gate layout when an old stargate is loaded at startup.

debug: Whether to show massive debug output
permdebug: Whether to show massive permission debug output
```

# Message Customization
It is possible to customize all of the messages Stargate displays, including the [Stargate] prefix. You can find the strings in plugins/Stargate/lang/en.txt. 

If a string is removed, or left blank, it will not be shown when the user does the action associated with it.
There are three special cases when it comes to messages, these are:
```
ecoDeduct=Spent %cost%
ecoRefund=Refunded %cost%
ecoObtain=Obtained %cost% from Stargate %portal%
```
As you can see, these three strings have %cost% and %portal% variables in them. These variables are fairly self-explanatory.

The full list of strings is as follows:
```
prefix=[Stargate] 
teleportMsg=Teleported
destroyMsg=Gate Destroyed
invalidMsg=Invalid Destination
blockMsg=Destination Blocked
denyMsg=Access Denied
destEmpty=Destination List Empty

ecoDeduct=Deducted %cost%
ecoRefund=Redunded %cost%
ecoObtain=Obtained %cost% from Stargate %portal%
ecoInFunds=Insufficient Funds

createMsg=Gate Created
createNetDeny=You do not have access to that network
createPersonal=Creating gate on personal network
createNameLength=Name too short or too long.
createExists=A gate by that name already exists
createFull=This network is full
createWorldDeny=You do not have access to that world
createConflict=Gate conflicts with existing gate
```

# Changes
#### [Version 0.10.9.1] UNIFIED LEGACY ESR -- 1.13.2 - 1.16.5
 - Backported this plugin to 1.16.5
 - This branch will now receive MINIMAL MAINTENANCE.
#### [Version 0.10.8.1] LCLO Fork
 - Fixed a compilation error impacting fresh installs.
#### [Version 0.10.8.0] LCLO Fork
 - Improved configuration
 - Added support for vanilla sign colours
 - Added support for closed economies
 - Allowed button/coral customisation
 - Hid the purple beacon from end_gateway
 - Migrated to new Vault repository
 - Added Chinese translations
 - Added a warning for spawn protection interference
 - Fixed an issue with portal verification
 - Fixed an issue with bstats
 - Facilitated a [specific use case](https://git.io/Jle4w)
#### [Version 0.10.7.2] LCLO Fork
 - Corrects some minor structure issues
#### [Version 0.10.7.0] LCLO Fork
 - Added additional informative warning/error messages
 - Added support for tags
 - Tested on minecraft 1.17.0 with no known issues
 - More bstats metrics
#### [Version 0.10.6.0] LCLO Fork
 - Cleaned up some extremely inefficient and outdated code.
 - Added some comments to make the code more legible.
 - Updated metrics to bstats framework.
 - Redid the language loader to completely fix issues with displaying foreign languages
#### [Version 0.10.5.0] LCLO Fork
 - Fixed a language enumeration glitch
 - Cleaned upp the LangLoader class
 - Now works with caveair
 - More restrictive portal open/create params
 - Initial startup now generates an example underwater gate file
#### [Version 0.10.4.0] LCLO Fork
 - Majorly refactored the code.
 - Fixed a bug with underwater networked portals
 - Allowed networked portals to target fixed portals
 - Fixed an issue with permissions
 - Fixed an incompatibility with RestrictedCreative
 - Fixed a possible null pointer
 - Updated translation footers to account for Bungee support
 - Renamed Portugese for uniform compliance with ISO-639-1
 - Updated translations for French, Dutch, Spanish, and German for Bungee support
 - Added a new Swedish translation.
#### [Version 0.10.3.0] LCLO Fork
 - Merged PseudoKnight upstream changes.
#### [Version 0.10.2.0] LCLO Fork
 - Updated to 1.16.3
#### [Version 0.10.1.0] LCLO Fork
 - Merged LittleBigBug downstream changes
 - Merged PseudoKnight upstream Changes
#### [Version 0.9.4.0] PseudoKnight Fork
(Packaged as 0.8.0.2)
 - Fixed player relative yaw when exiting portal
 - Add color code support in lang files
#### [Version 0.9.3.0] LittleBigBug Fork
(Packaged as 0.9.2.8)
 - Major code cleanup
#### [Version 0.9.2.0]
 - Fixed some bugs that prevented random teleportation
 - Added support for underwater portals
#### [Version 0.9.1.0]
 - Minor changes
#### [Version 0.9.0.0] LCLO Fork
 - Updated to 1.15 compatibility
#### [Version 0.8.0.0] PseudoKnight fork
 - Update for 1.13/1.14 compatibility. This changes gate layouts to use new material names instead of numeric ids. You need to update your gate layout configs.
 - Adds "verifyPortals" config option, which sets whether an old stargate's blocks are verified when loaded.
 - Adds UUID support. (falls back to player names)
#### [Version 0.7.9.11] PseudoKnight fork
 - Removed iConomy support. Updated Vault support. Changed setting from "useiconomy" to "useeconomy".
 - Updated to support Metrics for 1.7.10
#### [Version 0.7.9.10]
 - Fix personal gate permission check for players with mixed-case names
#### [Version 0.7.9.9]
 - Remove "Permissions" support, we now only support SuperPerms handlers.
#### [Version 0.7.9.8]
 - Make sure buttons stay where they should
#### [Version 0.7.9.7]
 - Do the Bungee check after the gate layout check.
#### [Version 0.7.9.6]
 - Actually remove the player from the BungeeQueue when they connect. Oops :)
 - Implement stargate.server nodes
 - Improve the use of negation. You can now negate networks/worlds/servers while using stargate.use permissions.
#### [Version 0.7.9.5]
 - Fixed an issue with portal material not showing up (Oh, that code WAS useful)
#### [Version 0.7.9.4]
 - Fixed an issue where water gates broke, oops
#### [Version 0.7.9.3]
 - Update BungeeCord integration for b152+
#### [Version 0.7.9.2]
 - Remove my custom sign class. Stupid Bukkit team.
 - Will work with CB 1.4.5 builds, but now will break randomly due to Bukkit screwup
 - Update MetricsLite to R6
#### [Version 0.7.9.1]
 - Optimize gate lookup in onPlayerMove
 - Resolve issue where Stargates would teleport players to the nether
#### [Version 0.7.9.0]
 - Added BungeeCord multi-server support (Requires Stargate-Bungee for BungeeCord)
 - Updated Spanish language file
 - Added basic plugin metrics via http://mcstats.org/
 - Resolve issue where language updating overwrote custom strings
#### [Version 0.7.8.1]
 - Resolve issue of language file being overwritten as ANSI instead of UTF8
#### [Version 0.7.8.0]
 - Updated languages to include sign text (Please update any languages you are able!)
 - Resolved NPE due to Bukkit bug with signs
 - Resolved issue regarding new getTargetBlock code throwing an exception
 - Languages now auto-update based on the .JAR version (New entries only, doesn't overwrite customization)
 - New command "/sg about", will list the author of the current language file if available 
 - Language now has a fallback to English for missing lines (It's the only language I can personally update on release)
 - Added Spanish (Thanks Manuestaire) and Hungarian (Thanks HPoltergeist)
 - Added portal.setOwner(String) API
#### [Version 0.7.7.5]
 - Resolve issue of right clicking introduced in 1.3.1/2
#### [Version 0.7.7.4]
 - Removed try/catch, it was still segfaulting. 
 - Built against 1.3.1
#### [Version 0.7.7.3]
 - Wrap sign changing in try/catch. Stupid Bukkit
#### [Version 0.7.7.2]
 - Load chunk before trying to draw signs
 - Implement a workaround for BUKKIT-1033
#### [Version 0.7.7.1]
 - Permission checking for 'R'andom gates.
 - Random now implies AlwaysOn
 - Added all languages to JAR
#### [Version 0.7.7.0]
 - Added 'R'andom option - This still follows the permission rules defined for normal gate usage
 - Added a bit more debug output
#### [Version 0.7.6.8]
 - Hopefully fix backwards gate exiting
#### [Version 0.7.6.7]
 - Reload all gates on world unload, this stops gates with invalid destinations being in memory.
#### [Version 0.7.6.6]
 - Check move/portal/interact/signchange events for cancellation
#### [Version 0.7.6.5]
 - Resolve issue with buttons on glass gates falling off
 - /sg reload can now be used ingame (stargate.admin.reload permission)
#### [Version 0.7.6.4]
 - Move blockBreak to HIGHEST priority, this resolves issues with region protection plugins
#### [Version 0.7.6.3]
 - Fixed issue with displaying iConomy prices
 - iConomy is now hooked on "sg reload" if not already hooked and enabled
 - iConomy is now unhooked on "sg reload" if hooked and disabled
#### [Version 0.7.6.2]
 - Button now activates if gate is opened, allowing redstone interaction
 - Fixed issue with sign line lengths. All sign text should now fit with color codes.
#### [Version 0.7.6.1]
 - Update API for StargateCommand
 - Resolved issue with block data on explosion
 - Added signColor option
 - Added protectEntrance option
#### [Version 0.7.6]
 - Moved gate opening/closing to a Queue/Runnable system to resolve server lag issues with very large gates
#### [Version 0.7.5.11]
 - PEX now returns accurate results without requiring use of the bridge.
#### [Version 0.7.5.10]
 - Added sortLists options
#### [Version 0.7.5.9]
 - Quick event fix for latest dev builds
 - Fix for sign ClassCastException
#### [Version 0.7.5.8]
 - Fixed an exploit with pistons to destroy gates
#### [Version 0.7.5.7]
 - Removed SignPost class
 - Resolved issues with signs in 1.2
#### [Version 0.7.5.6]
 - Quick update to the custom event code, works with R5+ now.
#### [Version 0.7.5.5]
 - PEX is built of fail, if we have it, use bridge instead.
#### [Version 0.7.5.4]
 - Fix issue with private gates for players with long names
#### [Version 0.7.5.3]
 - Added another check for Perm bridges.
#### [Version 0.7.5.2]
 - Make sure our timer is stopped on disable
 - Move Event reg before loading gates to stop portal material vanishing
#### [Version 0.7.5.1]
 - Don't create button on failed creation
#### [Version 0.7.5.0]
 - Refactored creation code a bit
 - Added StargateCreateEvent, see Stargate-API for usage.
 - Added StargateDestroyEvent, see Stargate-API for usage.
 - Updated Event API to the new standard, please see: http://wiki.bukkit.org/Introduction_to_the_New_Event_System
 - Added handleVehicles option.
 - Added 'N'o Network option (Hides the network from the sign)
#### [Version 0.7.4.4]
 - Changed the implementation of StargateAccessEvent.
 - Disable Permissions if version is 2.7.2 (Common version used between bridges)
 - Fix long-standing bug with hasPermDeep check. Oops.
#### [Version 0.7.4.3]
 - Implement StargateAccessEvent, used for bypassing permission checks/denying access to gates.
#### [Version 0.7.4.2]
 - stargate.create.personal permission now also allows user to use personal gates
#### [Version 0.7.4.1]
 - Quick API update to add player to the activate event
#### [Version 0.7.4.0]
 - Fixed issue with non-air closed portal blocks
 - Added StargatePortalEvent/onStargatePortal event
#### [Version 0.7.3.3]
 - Added "ignoreEntrance" option to not check entrance to gate on integrity check (Workaround for snowmen until event is pulled)
#### [Version 0.7.3.2]
 - Actually fixed "><" issue with destMemory
#### [Version 0.7.3.1]
 - Hopefully fixed "><" issue with destMemory
#### [Version 0.7.3]
 - Lava and water gates no longer destroy on reload
 - "sg reload" now closes gates before reloading
 - Added Vault support
 - Added missing "useiConomy" option in config
#### [Version 0.7.2.1]
 - Quick fix for an NPE
#### [Version 0.7.2]
 - Make it so you can still destroy gates in Survival mode
#### [Version 0.7.1]
 - Added destMemory option
 - Switched to sign.update() as Bukkit implemented my fix
 - Threw in a catch for a null from location for portal events
#### [Version 0.7.0]
 - Minecraft 1.0.0 support
 - New FileConfiguration implemented
 - Stop gates being destroyed on right-click in Creative mode
 - Fixed signs not updating with a hackish workaround until Bukkit is fixed
#### [Version 0.6.10]
 - Added Register support as opposed to iConomy
#### [Version 0.6.9]
 - Added UTF8 support for lang files (With or without BOM)
#### [Version 0.6.8]
 - Fixed unmanned carts losing velocity through gates
 - /sg reload now properly switches languages
#### [Version 0.6.7]
 - Added lang option
 - Removed language debug output
 - Added German language (lang=de) -- Thanks EduardBaer
#### [Version 0.6.6]
 - Added %cost% and %portal% to all eco* messages
 - Fixed an issue when creating a gate on a network you don't have access to
#### [Version 0.6.5]
 - Moved printed message config to a seperate file
 - Added permdebug option
 - Hopefully fix path issues some people were having
 - Fixed iConomy creation cost
 - Added 'S'how option for Always-On gates
 - Added 'stargate.create.gate' permissions
#### [Version 0.6.4]
 - Fixed iConomy handling
#### [Version 0.6.3]
 - Fixed (Not Connected) showing on inter-world gate loading
 - Added the ability to negate Network/World permissions (Use, Create and Destroy)
 - Fixed Lockette compatibility
 - More stringent verification checks
#### [Version 0.6.2]
 - Fixed an issue with private gates
 - Added default permissions
#### [Version 0.6.1]
 - Stop destruction of open gates on startup
#### [Version 0.6.0]
 - Completely re-wrote Permission handling (REREAD/REDO YOUR PERMISSIONS!!!!!!!!)
 - Added custom Stargate events (See Stargate-DHD code for use)
 - Fixed portal event cancellation
 - Umm... Lots of other small things.
#### [Version 0.5.5]
 - Added 'B'ackwards option
 - Fixed opening of gates with a fixed gate as a destination
 - Added block metadata support to gates
#### [Version 0.5.1]
 - Take into account world/network restrictions for Vehicles
 - Properly teleport empty vehicles between worlds
 - Properly teleport StoreageMinecarts between worlds
 - Take into account vehicle type when teleporting
#### [Version 0.5.0]
 - Updated the teleport method
 - Remove always-open gates from lists
 - Hopefully stop Stargate and Nether interference
#### [Version 0.4.9]
 - Left-click to scroll signs up
 - Show "(Not Connected)" on fixed-gates with a non-existant destination
 - Added "maxgates" option
 - Removed debug message
 - Started work on disabling damage for lava gates, too much work to finish with the current implementation of EntityDamageByBlock
#### [Version 0.4.8]
 - Added chargefreedestination option
 - Added freegatesgreen option
#### [Version 0.4.7]
 - Added debug option
 - Fixed gates will now show in the list of gates they link to.
 - iConomy no longer touched if not enabled in config
#### [Version 0.4.6]
 - Fixed a bug in iConomy handling.
#### [Version 0.4.5]
 - Owner of gate now isn't charged for use if target is owner
 - Updated for iConomy 5.x
 - Fixed random iConomy bugs
#### [Version 0.4.4]
 - Added a check for stargate.network.*/stargate.world.* on gate creation
 - Check for stargate.world.*/stargate.network.* on gate entrance
 - Warp player outside of gate on access denied
#### [Version 0.4.3]
 - Made some errors more user-friendly
 - Properly take into account portal-closed material
#### [Version 0.4.2]
 - Gates can't be created on existing gate blocks
#### [Version 0.4.1]
 - Sign option permissions
 - Per-gate iconomy target
 - /sg reload command
 - Other misc fixes
#### [Version 0.4.0]
 - Carts with no player can now go through gates.
 - You can set gates to send their cost to their owner.
 - Per-gate layout option for "toOwner".
 - Cleaned up the iConomy code a bit, messages should only be shown on actual deduction now.
 - Created separate 'stargate.free.{use/create/destroy}' permissions.
#### [Version 0.3.5]
 - Added 'stargate.world.*' permissions
 - Added 'stargate.network.*' permissions
 - Added 'networkfilter' config option
 - Added 'worldfilter' config option
#### [Version 0.3.4]
 - Added 'stargate.free' permission
 - Added iConomy cost into .gate files
#### [Version 0.3.3]
 - Moved sign update into a schedule event, should fix signs
#### [Version 0.3.2]
 - Updated to latest RB
 - Implemented proper vehicle handling
 - Added iConomy to vehicle handling
 - Can now set cost to go to creator on use
#### [Version 0.3.1]
 - Changed version numbering.
 - Changed how plugins are hooked into.
#### [Version 0.30]
 - Fixed a bug in iConomy checking.
#### [Version 0.29]
 - Added iConomy support. Currently only works with iConomy 4.4 until Niji fixes 4.5
 - Thanks @Jonbas for the base iConomy implementation
#### [Version 0.28]
 - Fixed an issue with removing stargates during load
#### [Version 0.27]
 - Fixed portal count on load
#### [Version 0.26]
 - Added stargate.create.personal for personal stargate networks
 - Fixed a bug with destroying stargates by removing sign/button
#### [Version 0.25]
 - Fixed a bug with worlds in subfolders
 - Fixed gates being destroyed with explosions
 - Added stargate.destroy.owner
#### [Version 0.24]
 - Fixed a loading bug in which invalid gates caused file truncation
#### [Version 0.23]
 - Added a check to make sure "nethergate.gate" exists, otherwise create it
#### [Version 0.22]
 - Fixed multi-world stargates causing an NPE
#### [Version 0.21]
 - Code cleanup
 - Added a few more errors when a gate can't be loaded
 - Hopefully fixed path issue on some Linux installs
#### [Version 0.20]
 - Fixed the bug SIGN_CHANGE exception when using plugins such as Lockette
#### [Version 0.19]
 - Set button facing on new gates, fixes weirdass button glitch
 - Beginning of very buggy multi-world support
#### [Version 0.18]
 - Small permissions handling update.
#### [Version 0.17]
 - Core GM support removed, depends on FakePermissions if you use GM.
#### [Version 0.16]
 - Fixed Permissions, will work with GroupManager, Permissions 2.0, or Permissions 2.1
 - Left-clicking to activate a stargate works again
#### [Version 0.15]
 - Built against b424jnks -- As such nothing lower is supported at the moment.
 - Moved gate destruction code to onBlockBreak since onBlockDamage no longer handles breaking blocks.
 - Removed long constructor.
#### [Version 0.14]
 - Fixed infinite loop in fixed gates.
 - Fixed gate destination will not open when dialed into.
#### [Version 0.13]
 - Fixed gates no longer show in destination list.
#### [Version 0.12]
 - Implemented fixed destination block using * in .gate file. This is the recommended method of doing an exit point for custom gates, as the automatic method doesn't work in a lot of cases.
 - Split networks up in memory, can now use same name in different networks. As a result, fixed gates must now specify a network.
 - Added the ability to have a private gate, which only you can activate. Use the 'P' option to create.
 - Fixed but not AlwaysOn gates now open the destination gate.
 - Fixed gates now show their network. Existing fixed gates are added to the default network (Sorry! It had to be done)
#### [Version 0.11]
 - Fuuuu- Some code got undid and broke everything. Fixed.
#### [Version 0.10]
 - Hopefully fixed the "No position found" bug.
 - If dest > origin, any blocks past origin.size will drop you at dest[0]
 - Switched to scheduler instead of our own thread for closing gates and deactivating signs
 - No longer depend on Permissions, use it as an option. isOp() used as defaults.
#### [Version 0.09]
 - Gates can now be any shape 
#### [Version 0.08]
 - Gates can now consist of any material.
 - You can left or right click the button to open a gate
 - Gates are now initialized on sign placement, not more right clicking!
#### [Version 0.07]
 - Fixed where the default gate is saved to.
#### [Version 0.06]
 - Forgot to make gates load from new location, oops
#### [Version 0.05]
 - Moved Stargate files into the plugins/Stargate/ folder
 - Added migration code so old gates/portals are ported to new folder structure
 - Create default config.yml if it doesn't exist
 - Fixed removing a gate, it is now completely removed
#### [Version 0.04]
 - Updated to multi-world Bukkit
#### [Version 0.03]
 - Changed package to net.TheDgtl.*
 - Everything now uses Blox instead of Block objects
 - Started on vehicle code, but it's still buggy
