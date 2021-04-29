# Adding custom structures

You'd love to see your own structures appear in new worlds? We've got your back!

To add a custom structure using the [Shrines mod](https://www.curseforge.com/minecraft/mc-mods/shrines-structures) there are two ways, but both start same:

1. First build your structure!
2. Decide how your structure should be named
3. Export your structure to a file using structure-blocks (vanilla)
4. Start minecraft at least one time with the latest version of the mod

Here you have to decide:
- Do you want to add the structure manually via filesystem
- Do you want to add them via using commands

### Commands

5. Use the command "/shrines-structures" to add your structures

5.1. Add the structure with "/shrines-structures add |name of your structure| |seed|", while |name of your structure should be replaced with the name of step 2 and |seed| with an integer (The seed decides where your structure will be placed in the world, but don't car eabout it and set it to any random value)
!IMPORTANT! Never use two time the same seed. This would cause the structures to appear in each other

5.2. Type "/shrines-structures query" to see a full list of custom structures that you've added

5.3. Type "/shrines-structures query |name of your structure|" to see the actual configuration of your structure

5.4. Use "/shrines-structures configure |name of your structure| |option| |value|" to change same values of the configuration of point 5.3

|option| is one of the names in the output of point 5.3 before the ":"

|value| is the value beloning to the |option| above. Only same types as default are supported (e.g. an integer can't be set to an boolean or an string can't be set to an integer)

6. Restart your Minecraft

7. Navigate to your Minecarft Directory ([Help](https://help.minecraft.net/hc/en-us/articles/360035131551-Where-are-Minecraft-files-stored-))

8. Open up the directory "shrines-saves"

9. Check weather the structure was setted up correctly. Here should now be a file called "structures.txt" and a folder "shrines"

10. Open now the folder "shrines". Here should be a folder with the name of your structure. Open this up

11. Here is a file with the name of your structure. Here you could change any settings. Search for "Pieces:". After this line there should be a line "[|name of your structure|, [0,0,0]]"

12. Change the content of |name of your structure| to the name of your exported *.nbt file (Without extension)

13. Put your *.nbt file next to the |name of your structure|.txt file (Do have a copy of this everytime to prevent data loss because of bugs)

14. Restart your Minecarft again. Your structures should now appear in new worlds (test with "/locate shrines:|name of your structure|")

### Manually

5. Here you do mainly the same as in the way with commands, but you create all the directories and files on your own

5.1. Navigate to your Minecarft Directory ([Help](https://help.minecraft.net/hc/en-us/articles/360035131551-Where-are-Minecraft-files-stored-))

5.2 open up the directory "shrines-saves"

5.3 create here a file named "structures.txt" (If it doesn't still exists)

5.4 Put in each line of the file a name of a new structure (lower-case, no special character, no brackets)

5.5 create a folder named "shrines"

5.6 Create for each of the new structures a new folder inside "shrines" with the same name as the structure

5.7 Create in each of these folder a file named |name of the structure|.txt and copy in this file the default config 

> Seed:0
> Generate:true
> Spawn Chance:0.6
> Needs Ground:true
> Distance:50
> Seperation:8
> #Put in here all biomes that you want your structure to spawn in. DEFAULT and ALL are also supported; Sepperate them by ","
> Biome Categories:[DEFAULT]
> #Put here all biomes that you want your structure NOT to spawn in. Use minecraft name keys
> Biome Blacklist:[]
> Use Random Varianting:true
> Pieces:
> [|name of your structure|, [0,0,0]]

5.8 Make sure to change seed (See Commands/5.1 for more information) and |name of your structure|

5.9 Copy the *.nbt files in the associated folder and rename them to |name of your structure|.nbt

6.0 Restart your Minecarft again. Your structures should now appear in new worlds (test with "/locate shrines:|name of your structure|")

### Multiple .nbt files

To have multiple .nbt files in one structure isn't hard to do. You have to add one line (for each additional file) to your config file of the structure. Search there for the option "Pieces:". After this line, there should be a line named "[|name of your structure|, [0,0,0]]". Add a "," at the end of this line to mark, that there is one more possible file. Copy this line as often as you need. Change the content of |name of your structure| for each line to the name of one of your .nbt files. Make sure that the last line doesn't have a ",". That would cause syntax errors

The content of the second brackets ([0,0,0]) is an offset position from the original generation position, so you can adjust the distance between the .nbt files. (Note that it isn't the 'distance' between them, but it helps to create the correct distance between them
