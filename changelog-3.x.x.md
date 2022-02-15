## 3.0.0-Beta15 --- 15.02.2022 15:24 UTC

- Fixed issue with "" in spanish translations
- Set start pool of upgraded structures from 1.8.1
- Added Translations for control configuration options of shrines mod

===========

## 3.0.0-Beta14 --- 13.02.2022 20:35 UTC

- Fix warning 'Potentially Dangerous alternative prefix shrines for name trader_house, expected minecraft. This could be
  a intended override, but in most cases indicates a broken mod.'

===========

## 3.0.0-Beta13 --- 06.02.2022 15:26 UTC

- Improved structure generation in the end (check 4 edge points in radius of 20 blocks for spawn height)
- Fixed watchtower destroys ground one level under it (structure void issue)

===========

## 3.0.0-Beta12 --- 04.02.2022 16:40 UTC

- Added End Temple's novel
- Fixed dimensions whitelist UI
- Improved legacy 1.8.1 structure configuration import
- require start pool to be set to allow generation

===========

## 3.0.0-Beta11 --- 03.02.2022 17:30 UTC

- Fixed error message when import of legacy packages failed because of an IOError while reading structures.txt
- Fixed crash in case specified start pool doesn't exist
- Changed legacy 1.8.1 load conditions: 'shrines-saves' must exist and either 'structures.txt' or 'shrines-server.toml'
  in config directory
- Improved legacy 1.8.1 load: Load included structure's config too

===========

## 3.0.0-Beta10 --- 31.01.2022 20:40 UTC

- We moved our wiki. Included new link in the mod

===========

## 3.0.0-Beta9 --- 24.01.2022 15:35 UTC

- Added Novels:
    - Player House
    - Small Temple
    - Mineral Temple
    - Water Shrine
- Fixed crash when opening structure insights screen, but you've found the structure more often then there are parts of
  the novel

===========

## 3.0.0-Beta8 --- 12.01.2022 17:09 UTC

- Fixed wrong warning about structure icon cache error

===========

## 3.0.0-Beta7 --- 11.01.2022 19:34 UTC

- Reset Random variation remaps on world save instead of world load

===========

## 3.0.0-Beta6 --- 11.01.2022 14:47 UTC

- Fixed loading client classes on dedicated server (close #32 and #33 may #26 too)

===========

## 3.0.0-Beta5 --- 10.01.2022 14:30 UTC

- Internal rewrite
- changed save format of structure packages
- added backwards compatible package loaders
- Added new options to structure's config: iconPath and jigsawMaxDepth
- Improved error splashes in package configuration GUI
- Structures are saved in JSON files
- Packages aren't saved per default -> added save button
- Updated link to shrines wiki
- Fixed typos in german translations
- Changed Novels Data saved format
- Found Novels are now indicated per player and no longer per world
- Added Novel of Shrine of Savanna and Nether pyramid
- Random Variation is back
- Added new configuration options for each structure to configure the variation
- removed 'use_random_varianting' option
- Added error message to biome selection if the current biome doesn't have a registry name
- Enhanced Structure Configuration Screen with toggleable categories
- Changed key of separation factor (from 'seperation_factor' to 'separation_factor')

===========

## 3.0.0-Beta4 --- 21.12.2021 17:15 UTC

- Fixed some english translations
- Added traditional chinese translations

===========

## 3.0.0-Beta3 --- 19.12.2021 06:13 UTC

- Fixed item Size slider in structure novel screen
- Fixed #25
- Updated to 1.18.1

===========

## 3.0.0-Beta2 --- 10.12.2021 09:44 UTC

- Added loot table for watchtower
- Make use of the new updateChecker place

===========

## 3.0.0-Beta1 --- 10.12.2021 04:15 UTC

- Based on 2.0.0-Beta6
- Removed unused ClearImagesCachePacket
- Removed unused checkbox button
- Changed behavior of icon size slider in novels screen