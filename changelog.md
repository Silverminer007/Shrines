## 4.1.0

- Add optional parameter to `shrines:relative` placement calculator
  - `heightmap`. Possible values:
    - `WORLD_SURFACE_WG`
    - `WORLD_SURFACE`
    - `OCEAN_FLOOR_WG`
    - `OCEAN_FLOOR`
    - `MOTION_BLOCKING`
    - `MOTION_BLOCKING_NO_LEAVES`

===========

# 4.0.0

**User facing changes**
- Add new structures:
  - `mayan_temple`
  - `oriental_hut`
  - `luxury_villa`
  - `azalea_pavilion`
- Add commands:
  - `locateshrines`
  - `variate`
- removed structure Config GUI
- Added more `ware` templates to the harbour
- remove structure novels
- remove structure Config GUI
- split player house in `small_player_house` and `tall_player_house`

**technical changes**
- structures are now fully data-driven
  - Add two new structure types `shrines:surface` and `shrines:underground`
  - This types can be configured with these options:
    - `start_pool`
    - `size` (max jigsaw depth)
    - `spawn_criteria` (basic requirements for a structure to consider a place valid). Possible values:
      - `close_to_structure`, further options: `structure_set`, `min_chunk_distance`
      - `not_close_to_structure`, further options: `structure_set`, `min_chunk_distance`
      - `random_chance`, further options: `spawn_chance`
      - `height`, further options: `min_height`, `max_height`, `check_size`
      - `ground_level_delta`, further options: `delta`, `check_size`
      - `min_structure_distance`, further options: `range`
    - `placement_calculator` (the y level of the structure). Possible values:
      - `simple`
      - `fixed`, further options: `height`
      - `first_free`
      - `relative`, further options: `offset`
- Structure Config (the toml file) has other options now. See comments in file for more info
  - `removed_structures`
  - `disabled_structures`
  - `run_structure_updater`
  - `min_structure_distance`

**See [changelog](https://github.com/Silverminer007/Shrines/blob/1.18.2%2B-4.x.x/changelog.md) for more details**

===========

## 4.0.0-beta4

**If no major issues arise, this is going to be the last beta version before stable release**

- Add Mayan Temple to appropriate tags
- Add Tag Support for Random Variation Config/Material
- Add Random Variation Material Tags `#shrines:any`, `#shrines:wood`, `#shrines:colour`, `#shrines:stone`, `#shrines:bees`, `#shrines:ore` and `#shrines:light`
- Add Random Variation Configs for all Shrines Structures
- Include also not data-generated resources in Default Config ZIP
- Shrines requires Forge 40.1.35 or above

**Changes are non-breaking in most cases. Please report issues [here](https://github.com/Silverminer007/Shrines/issues)**

===========

## 4.0.0-beta3

- It's pride month!
- Fix mayan temple spawn

**Changes are non-breaking in most cases. Please report issues [here](https://github.com/Silverminer007/Shrines/issues)**

===========

## 4.0.0-beta2

- Added Mayan Temple ('mayan_temple')

**Changes are non-breaking in most cases. Please report issues [here](https://github.com/Silverminer007/Shrines/issues)**

===========

## 4.0.0-beta1

- Bump Shrines for 1.18.2 to Beta stage
- Upload Default Configurations to GitHub releases

**Changes are non-breaking in most cases. Please report issues [here](https://github.com/Silverminer007/Shrines/issues)**

===========

### 4.0.0-alpha11

**This is an alpha release; breaking changes can happen at any time. Don't use this file in production**

- Add 'locateshrines' command
  - Use literal 'inbiome' to locate a structure in a specified biome
  - Use literal 'new' to locate a not yet generated structure

**Feedback is appreciated**

===========

### 4.0.0-alpha10

**This is an alpha release; breaking changes can happen at any time. Don't use this file in production**

- Add Structure Min Distance spawn criteria. Value -1 redirects to global structure_min_distance config option (Fix [#41](https://github.com/Silverminer007/Shrines/issues/41))
- Fix Structure void issues with balloon and small player house
- Add a few missing balloon variants
- Introduce '#shrines:any' structure tag to locate any structure of shrines
  - Introduce '#shrines:house', '#shrines:temple', '#shrines:shrine', '#shrines:aurelj', '#shrines:chptr1', '#shrines:forscher09', '#shrines:lady_jessa', '#shrines:meme_man_77', '#shrines:s1fy', '#shrines:sam_hit_apple', '#shrines:silverminer', '#shrines:tikofan'
- Remove references to 'shrines:harbour/villagers' pool
- Add more 'ware' variants to the harbour

**Feedback is appreciated**

===========

### 4.0.0-alpha9

**This is an alpha release; breaking changes can happen at any time. Don't use this file in production**

- Automatically update old custom structures to the new structure system
- Update old structure configs for shrines default structures to the new system
- For information see here: https://silverminer007.github.io/ShrinesWiki/wiki/en_us/users/structureUpdater1.18.2.html

**Feedback is appreciated**

===========

### 4.0.0-alpha8

**See https://silverminer007.github.io/ShrinesWiki/wiki/en_us/users/updateTo4.x.x.html for update instructions**

**This is an alpha release; breaking changes can happen at any time. Don't use this file in production**

- Add "disabled structures" config option. See this article for instructions: https://silverminer007.github.io/ShrinesWiki/wiki/en_us/users/disableStructure.html

**Feedback is appreciated**

===========
### 4.0.0-alpha7

**See https://silverminer007.github.io/ShrinesWiki/wiki/en_us/users/updateTo4.x.x.html for update instructions**

**This is an alpha release; breaking changes can happen at any time. Don't use this file in production**

- Add oriental Hut (Credits to lady_jessa (aka LadyJessa) for building)
- Add missing "empty" biome tag

**Feedback is appreciated**

===========
### 4.0.0-alpha6

**See https://silverminer007.github.io/ShrinesWiki/wiki/en_us/users/updateTo4.x.x.html for update instructions**

**This is an alpha release; breaking changes can happen at any time. Don't use this file in production**

- requires Forge 40.1.0 or above
- removed a few biomes from bees' biome generation whitelist
- re-implement random variation. Auto convert old random variation material files to new versions
  - Add random variation configs to define pools of possible rempas (e.g. all oak becomes spruce sometimes)
    - Add configs for abandoned_villa, abandoned_witch_house, azalea_pavilion, bees, flooded_temple and guardian_meeting (more are in work)
    - These are configurable through datapacks
  - Add 53 default random variation materials
- Allow updates from old worlds. Custom structures most either be added back manually or added to the removed structures list in shrines' new config
- Fix Harbour spawn doesn't work, due to an empty start pool
- Added `/variate` command. It runs random variation over either an already existing part of the world or over a new generated structure from template/pool
  - This feature is still in development/wip. Please consider this while using
- Added Game Test for dynamic registries (more test are planned)
- Log exceptions when a command fails with an unexpected exception

**Feedback is appreciated**

===========
### 4.0.0-alpha5

**It's not possible to update old worlds and installations to this version**
**This is an alpha release; breaking changes can happen at any time. Don't use this file in production**

- fixed issue with ground below azalea pavilion
- add new placement calculator type 'shrines:relative'

**Feedback is appreciated**

===========
### 4.0.0-alpha3

**It's not possible to update old worlds and installations to this version**
**This is an alpha release; breaking changes can happen at any time. Don't use this file in production**

- required Forge 40.0.36+
- Add lady_jessa's luxury villa model of the modern villa
- Implement (missed) Ground level delta spawn criteria
- Add default spawn criteria to all structures (ground level delta)
- Change harbour's biomes to oceans again
- Renamed watch tower's and oriental sanctuary's template
- Add missing templates to nether shrine's template pool
- Add missing biome tag for world tree manor

**Feedback is appreciated**

===========
### 4.0.0-alpha2

**It's not possible to update old worlds and installations to this version**
**This is an alpha release; breaking changes can happen at any time. Don't use this file in production**

- Player house split up into small player house and tall player house (moved templates)
- removed player house table pool
- modified small player house's template
- removed dragon heads from harbour houses
- abandoned villa and modern villa merged to one structure set 'modern_villa'
- mineral temple, flooded temple, high temple and small temple merged to structure set 'temples'
- added azalea pavilion
- Added missing biome tags

**Feedback is appreciated**

===========
### 4.0.0-alpha1

**It's not possible to update old worlds and installations to this version**
**This is an alpha release; breaking changes can happen at any time. Don't use this file in production**

- Ported to 1.18.2 based on 3.0.0-Beta22
- Removed random variation and structure novels for now (they will come back later)
- removed structure config GUI
- removed shrines toml config

**Feedback is appreciated**