## 2.2.0-Beta1 --- 14.02.2022 18:50 UTC

- Import old custom structures on startup too
- Fix import of old custom structures (#38)
- Use default structure config, if no config is present in old config for that structure

===========

## 2.1.0 --- 13.02.2022 13:43 UTC

- Import structure configurations from 1.8.1 or below to 2.1.0+

===========

## 2.0.1 --- 06.02.2022 15:50 UTC

- Fixed watchtower destroys ground one level under it (structure void issue)

===========

# 2.0.0 --- 03.02.2022 20:49 UTC

### Changes

- Added Structures: Watchtower, Trader House, Shrine of Savanna
- Improved structures: Bees, Harbour, Player House
- Changed structure configuration
- Changed custom structure configuration
- Removed '/shrines-structures' command
- Added Structure Novels
- Added Structure Configuration GUI
- Added new config option 'structure min. distance', 'banned_block' and 'banned_entities'
- Reworked config UI
- Added spanish and traditional chinese translations

### Bees

- Changes loot-table -> new items in chests
- Entirely new building now

### Harbour

- Use 'jigsaw system' now -> Modularised and randomised structure placement. Comparable with Minecraft's villages
- Spawns on oceans now
- New loot-table -> new items in chests
- New elements (e.g. new ways)

### Player House

- New loot-table -> new items in chests

### New Structure config & custom structure config & Structure Config UI

- Structures are bundled in packages
- Shrine brings 'Included Structures' Package with default configuration of structures
- Structure Configurations are saved per package in a structures.nbt file
- Config UI allows editing these packages
- Config UI can be opened in worlds with cheats enabled or on server by all players with op level 2 or higher
- Config UI (aka 'Admin Mode') access is hidden in extended Novels UI (see below)
- Structure Config now includes 'Templates' and 'Template Pools' (aka 'Pools'). Both are required for structures using
  the 'jigsaw system'
- Removed structure config option 'Needs Ground' and 'Spawn Villager'
- Added structure config options 'Transform Land'

### Structure Novels & Structure Novels UI

- Structure Novels belongs to a structure
- Structure Novels can be changed and added via structure configuration
- You'll find an excerpt of the novel every time you enter a structure
- Press 'key.customStructuresScreen' (Defaults to k) to open the Novels UI
- Press the arrow which is pointing down to extend bar on the Top to scale images and open 'Admin Mode' (aka 'Structure
  Config UI')

### 'structure min. distance'

- new Config Option
- Additional spawn criteria for shrine's structures
- Minimum distance to the next structure measured in chunks

### 'banned_blocks' and 'banned_entities'

- Structure placement post processor
- Removes all blocks/entities in the list from the structures at placement -> Can for example remove all dragon heads
  from the harbour

### Config UI vs Structure Config UI vs Structure Novels UI

- Config UI is accessible through Mod-list's 'Config' button
- Structure Config UI is accessible through Structure Novels UI
- Structure Novels UI is accessible in world and opens on Keybind press (defaulted to k)
- Config controls settings that apply to all structure or aren't related to structure themselves
- Structure Config controls settings related to structure and their generation
- Novels don't control anything and are only an additional gameplay addition

### Credits

- pea_sh0ter
- S1fy
- MEME Man77
- Forscher09 & XENZE2011

===========

## 2.0.0-rc2 --- 01.02.2022 14:42 UTC

- Updated link to wiki one more time (https://silverminer007.github.io/ShrinesWiki/)
- Introduced 'Deleted Structures' -> When you delete a structure in structure config GUI it isn't deleted entirely. It
  remains a '___ Deleted Structure ___' at the bottom of the list -> prevents log spam of 'Missing structure start:
  structure:key' -> You can still delete structures entirely, just delete the 'Deleted Structure'
- When updating from 1.8.1, we add 3 'Deleted Structures' with the old structure keys to prevent log spam

===========

## 2.0.0-rc1 --- 15.01.2022 17:46 UTC

- Updated link to shrines wiki
- Fixed some spelling mistakes in german translations

===========

## 2.0.0-Beta11 --- 09.01.2022 15.28 UTC

- Introduced Novels Part separator '|'
- Added Novel of Shrine of Savanna and Nether Pyramid
- Removed Config Option 'NEEDED_NOVELS', because parts are now have a fixed amount

===========

## 2.0.0-Beta10 --- 08.01.2022 16.07 UTC

- Fixed #30

===========

## 2.0.0-Beta9 --- 26.12.2021 16:05 UTC

- Rebuild 2.0.0-Beta8

===========

## 2.0.0-Beta8 --- 10.12.2021 04:15 UTC

- Fixed some english translations
- Added traditional chinese translations

===========

## 2.0.0-Beta7 --- 10.12.2021 09:59 UTC

- Added loot table to Watchtower
- Moved update checker and wiki to new repository

===========

## 2.0.0-Beta6 --- 28.11.2021 01:22 UTC

- Added missing structure icons of harbour and shrine of savanna
- Changed guardian meetings icon

===========

## 2.0.0-Beta5 --- 28.11.2021 01:13 UTC

- Replaced huge structure icons with 256x256 pixel versions -> mod files size is now much smaller
- Fixed default biome whitelist setting for shrine of savanna

===========

## 2.0.0-Beta4 --- 24.11.2021 06:55 UTC

- Added missing error splash title
- Added legacy structures packet import which allows you to import structures that you have created with versions below
  2.0.0 as structures packet in the new system

===========

## 2.0.0-Beta3 --- 20.11.2021 06:39 UTC

- Created Translation keys (that required some internal structure config option name changes, which could cause issues)
- Added English, german and Spanish Translation
- Increased size of the Admin Mode Button in Novels View to have enough space for text strings
- Made Packet list translatable and fixed pools amount display
- Fixed Structure Packet import throws error and sometimes crashes
- Increased Packet version

===========

## 2.0.0-Beta2 --- 10.11.2021 09:55 UTC

- Fix Images Cache wasn't cleaned (It's only cleared before a new bunch of images is cached, because hooks of player
  left worlds are fired to late)

===========

## 2.0.0-Beta1 --- 10.11.2021 09:11 UTC

- Reworked General Settings Screen (Fixed banned entities, banned blocks and biome blacklist options)

===========

## 2.0.0-SNAPSHOT-16 --- 10.11.2021 08:17 UTC

- Boolean Buttons now always toggle when pressed
- Fixed Dimensions being multiple times in activated dimensions
- Activated Pools Tab in Structure Packet Configuration screen
- Structure Packet Configuration is now done (Structures, Templates and Pools Tab works)
- Added new Structure option to assign start pool (Requires new packets to appear correctly)

===========

## 2.0.0-SNAPSHOT-15 --- 06.11.2021 02:57 UTC

- Added Error Splash screens
- Structure seed modifier shovel during packet import
- Structure Novels screen now only shows registered structures

===========

## 2.0.0-SNAPSHOT-14 --- 05.11.2021 06:23 UTC

- Add Templates Tab to upload structure templates (You can add them from your local file system, rename and delete)
- Fixed Structure Novels always appear empty
- Introduced High Temple's Novel (Credits to pea_sh0ter)
- Updated Mod description

===========

## 2.0.0-SNAPSHOT-13 --- 05.11.2021 00:23 UTC

- Fixed structure generation
- Changed back button texture to Novels screen version

===========

## 2.0.0-SNAPSHOT-12 --- 02.10.2021 10:18 UTC

- Added GUI for Structures -> Included and custom
- You're able to add/rename/delete/configure structure packets
- You're able to add/delete/configure structures
- You're able to add your own novels to the structures
- You're warned if a key is duplicated

===========

## 2.0.0-SNAPSHOT-11 --- 01.10.2021 10:18 UTC

- Fixed #19
- Added Structure Novel
- Added GUI to see Novels
- Changed config system of included structures
- Added Queue for structure packet editing to prevent conflicts

===========

## 2.0.0-SNAPSHOT-10 --- 28.08.2021 06:30 UTC

- Fixed #18
- Removed unused loot chance option
- Added Blame to dev env.

===========

## 2.0.0-SNAPSHOT-9 --- 14.08.2021 10:51 UTC

- Added 'banned_blocks' option
- Added 'banned_entities' option
- Changed pieces of the harbour
- Added some more debug logging
- Added S1fy's 'Shrine of savanna'

===========

## 2.0.0-SNAPSHOT-8 --- 27.07.2021 02:19 UTC

- Removed 'Needs Ground' option
- Removed 'Spawn Villagers' option
- Removed shrines structures command
- Fixed 'Dimensions' structure option
- Fixed terrain changes at the harbour and balloon
- Made template pools data driven
- Fixed 'Bees structure flies 1 block in the air'
- Added 'MEME Man 77's Watchtower
- Fixed 'dimensions' setting of structures
- Modified Harbour pieces
- Added comments to some config options (belongs to #16)
- Added loot table to the Trader House

===========

## 2.0.0-SNAPSHOT-7 --- 24.07.2021 09:48 UTC

- Added the new version of the Harbour (experimental and still in work)
- Fixed compiler error because of Java 16. Should work now (Fixes #15)

===========

## 2.0.0-SNAPSHOT-6 --- 23.07.2021 10:28 UTC

- Updated to forge 36.2.0

===========

## 2.0.0-SNAPSHOT-5 --- 02.07.2021 00:00 UTC

- Replaced 'bees' structure with a newer version of S1fy

===========

## 2.0.0-SNAPSHOT-4 --- 01.07.2021 00:00 UTC

- Added 'MEME Man77's Trader House

===========

## 2.0.0-SNAPSHOT-3 --- 29.06.2021 00:00 UTC

- Added Translations to "Structure min. Settings" config option
- Added new config option "Advanced Logging"(with translations) which allows to disable some (spamming) logs

===========

## 2.0.0-SNAPSHOT-2 --- 28.06.2021 00:00 UTC

- Added (configurable) min. distance that needs to be at least between two structure to allow generation

===========

## 2.0.0-SNAPSHOT-1 --- 28.06.2021 00:00 UTC

- Based on 1.8.1
- Changed structure generation system to jigsaw
- Changed Player House loot-tables