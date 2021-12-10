# 2.0.0

#### SNAPSHOT 1

- Changed structure generation system to jigsaw
- Changed Player House loot-tables

##### Problems:

- Nether structures generates over the bedrock ceiling
- Custom structures doesn't work right now (There are only balloons instead of the right resources)
- Harbour doesn't work (Same as customs)
- There's no (or less) variation in the structures

#### SNAPSHOT 2

- Added (configurable) min. distance that needs to be at least between two structure to allow generation

##### Problems:

- Previous problems still affect

#### SNAPSHOT 3

- Added Translations to "Structure min. Settings" config option
- Added new config option "Advanced Logging"(with translations) which allows to disable some (spamming) logs

##### Problems:

- Previous problems still affect

#### SNAPSHOT 4

- Added 'MEME Man77's Trader House

##### Problems:

- Previous problems still affect

#### SNAPSHOT 5

- Replaced 'bees' structure with a newer version of S1fy

##### Problems:

- Previous problems still affect

#### SNAPSHOT 6

- Updated to forge 36.2.0

##### Problems:

- Previous problems still affect

#### SNAPSHOT 7

- Added the new version of the Harbour (experimental and still in work)
- Fixed compiler error because of Java 16. Should work now (Fixes #15)

##### Problems

- Harbour contains Grass and dirt blocks which should be removed

#### SNAPSHOT 8

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

##### Problems

- No know new issues

#### SNAPSHOT 9

- Added 'banned_blocks' option
- Added 'banned_entities' option
- Changed pieces of the harbour
- Added some more debug logging
- Added S1fy's 'Shrine of savanna'

##### Problems

- Missed structure void in some pieces of the harbour so there is air instead of water

##### Notes

- New config options (banned blocks and banned entities) are only rarely tested, so major issue may arise

#### SNAPSHOT 10

- Fixed #18
- Removed unused loot chance option
- Added Blame to dev env.

##### Problems

- Issue #17 isn't fixed yet

#### SNAPSHOT 11

- Fixed #19
- Added Structure Novel
- Added GUI to see Novels
- Changed config system of included structures
- Added Queue for structure packet editing to prevent conflicts

##### Problems

- Issue #17 isn't fixed yet
- There are no Novels yet

#### SNAPSHOT 12

- Added GUI for Structures -> Included and custom
- You're able to add/rename/delete/configure structure packets
- You're able to add/delete/configure structures
- You're able to add your own novels to the structures
- You're warned if a key is duplicated

##### Problems

- Templates and Pools buttons points to empty screens
- Novels are always empty even if you add some in configuration
- Issue #17 isn't fixed yet
- There are no Novels yet

#### SNAPSHOT 13

- Fixed structure generation
- Changed back button texture to Novels screen version

##### Problems

- Previous Problems still affect

#### SNAPSHOT 14

- Add Templates Tab to upload structure templates (You can add them from your local file system, rename and delete)
- Fixed Structure Novels always appear empty
- Introduced High Temple's Novel (Credits to pea_sh0ter)
- Updated Mod description

##### Problems

- Previous Problems still affect

#### SNAPSHOT 15

- Added Error Splash screens
- Structure seed modifier shovel during packet import
- Structure Novels screen now only shows registered structures

##### Problems

- Previous Problems still affect

#### SNAPSHOT 16

- Boolean Buttons now always toggle when pressed
- Fixed Dimensions being multiple times in activated dimensions
- Activated Pools Tab in Structure Packet Configuration screen
- Structure Packet Configuration is now done (Structures, Templates and Pools Tab works)
- Added new Structure option to assign start pool (Requires new packets to appear correctly)

##### Problems

- Issue #17 was marked as fixed on GitHub, but issue was already fixed in earlier release

### Beta 1

- Reworked General Settings Screen (Fixed banned entities, banned blocks and biome blacklist options)

### Beta 2

- Fix Images Cache wasn't cleaned (It's only cleared before a new bunch of images is cached, because hooks of player
  left worlds are fired to late)

### Beta 3

- Created Translation keys (that required some internal structure config option name changes, which could cause issues)
- Added English, german and Spanish Translation
- Increased size of the Admin Mode Button in Novels View to have enough space for text strings
- Made Packet list translatable and fixed pools amount display
- Fixed Structure Packet import throws error and sometimes crashes
- Increased Packet version

#### Know Issues

- https://github.com/Silverminer007/Shrines/issues/22
- Some chars in spanish translations aren't properly displayed

### Beta 4

- Added missing error splash title
- Added legacy structures packet import which allows you to import structures that you have created with versions below
  2.0.0 as structures packet in the new system

### Beta 5

- Replaced huge structure icons with 256x256 pixel versions -> mod files size is now much smaller
- Fixed default biome whitelist setting for shrine of savanna

### Beta 6

- Added missing structure icons of harbour and shrine of savanna
- Changed guardian meetings icon

##### Notes

- These versions are beta builds so major issues may arise
- It's always a good idea to create a new structure packet if something does not work as expected

#### Outstanding

- Include a novel for every structure

# 3.0.0

### Beta 1

- Bumped Forge version to 38.0.0 (MC 1.18)
- Removed unused ClearImagesCachePacket
- Removed unused checkbox button
- Changed behavior of icon size slider in novels screen

## 1.8.1

### Beta1

- Shrine structures command was updated:
  -> Added 'save-resource' command option -> Added 'save' command option -> Added 'reset' command option -> Added 'load'
  command option -> Moved 'blacklist' & 'whitelist' of custom structures to improved command option -> Added alias for '
  shrines-structures' command ('sh-st')
- Added new config option to every structure
- Added properties file for configuring custom structure general properties and enable latest experimental features

### Beta2

- Added Aurelj's 'Oriental Sanctuary'
- Added 'dimensions' config option
- Fixed some generation height bugs
- Nether Shrine & Nether pyramid can be (configurable) spawn in the nether
- Added Config GUI
- Custom structure are now configure- and add-able via GUI
- You assign resources from worlds to your custom structures via GUI

### Beta3

- Added Translations
- Added new option to filter structures in config GUI
- Fixed crashes

### Beta4

- Fixed crashes

# 1.8.0

- Added Custom structures -> Added command to configure/add/remove/query custom structures
- Noobandius' mod 'lootr' is now compatible
- Improved harbour generation

## 1.7.1

- Added Tikofan's 'End Temple'
- Structures are locate-able on servers again

# 1.7.0

### Beta1

- Harbour is locate-able
- Added SamhitApple's 'InfestedPrison'
- Added SamhitApple's 'Abandoned Witch House'

### Beta2

- Added Villagers to the harbour
- Removed many anvils from the harbour
- Added loot-able chests and barrels to the harbour
- Added SamhitApple's 'Jungle Tower'
- Added S1fy's Guardian meeting
- Random varianting is now affected by biome

### Beta3

- Fixed crash when trying to generate structure

### Beta4

- SamhitApple's 'Jungle Tower' is now loot-able
- Modified worldgen a bit

# 1.6.0

- Added new version of the player house structure
- Added SamhitApple's MineralTemple
- Added SamhitApple's Flooded Temple
- Added more variants of the balloon (Over 400.000 at all)
- New config option 'needsGround' was introduced
- Introduced 'Random varianting'

## 1.5.2

- Added version support for 1.16.3-1.16.5
- Structures are now locate-able again

## 1.5.1

- Structure config improvements

# 1.5.0

- Added Player house
- Modified loot of some structures
- Added structure config
- Fixed bugs

## 1.4.2

- Merged PR#1 (fixed #1) - Game sometimes crashed when locating nether pyramid

## 1.4.1

- Nether pyramid & nether shrine are now loot-able

# 1.4.0

- Mc Update to 1.16.3
- Implemented VersionCheck

# 1.3.0

- Added more structures