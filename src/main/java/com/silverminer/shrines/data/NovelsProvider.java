package com.silverminer.shrines.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.silverminer.shrines.packages.datacontainer.StructureNovel;
import com.silverminer.shrines.worldgen.structures.variation.NewVariationMaterial;
import net.minecraft.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public record NovelsProvider(DataGenerator generator) implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger(NovelsProvider.class);
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    @Override
    public void run(@NotNull HashCache pCache) throws IOException {
        Path output = this.generator.getOutputFolder();

        Map<ResourceLocation, StructureNovel> novels = getNovels();
        for (ResourceLocation novel : novels.keySet()) {
            DataResult<JsonElement> dataResult = StructureNovel.CODEC.encode(novels.get(novel), JsonOps.INSTANCE, new JsonObject());
            Optional<JsonElement> optional = dataResult.resultOrPartial(LOGGER::error);
            if (optional.isPresent()) {
                ResourceLocation registryPath = StructureNovel.REGISTRY.location();
                DataProvider.save(GSON, pCache, optional.get(), output.resolve("data").resolve(novel.getNamespace()).resolve(registryPath.getPath()).resolve(novel.getPath() + ".json"));
            }
        }
    }

    private static @NotNull Map<ResourceLocation, StructureNovel> getNovels() {
        return Util.make(() -> {
            ImmutableMap.Builder<ResourceLocation, StructureNovel> builder = new ImmutableMap.Builder<>();
            builder.put(new ResourceLocation("shrines:high_temple"), new StructureNovel(
                    "We have finally made the altar. It is wonderful. I know that they will love it. So will the queen. If we can get more help from them, then we can finally beat “Them”. Who do they think they are, just breaking off and stealing all of our possessions? Luckily, the queen shared with us, so we are not dead. However…",
                    "the strange coral we found to make the lights is, how do I put it? Quite painful to get. We have to kill all these strange monsters to get it. Oh well, see you tomorrow diary!",
                    "Oh my! The monsters! We woke up to screaming in the night. Apparently, jon tried to harvest some of the crystals for the queen. Oh, it gets me so mad! He should like ME, not her. But that's not important. What is important is that",
                    "The monsters cursed us! The whole sea jumped up and swept the crops away! And now the land is cold. There is strange white stuff all over, and I am too cold to go outside. I do hope that the guards can protect us from the raids… Good night. I will write tomorrow.\n\n Oh. I have had a dream. It was from the leader of the betrayers! He said that I should join him. I had burst into laughter, but then he said that… He said he knew how to get jon to like me…\nHow I wish it were true. He had said that If I go to the alter and break the seal, then the cold will stay, but he will enchant jon to love me…"));
            builder.put(new ResourceLocation("shrines:nether_pyramid"), new StructureNovel(
                    "It has been done. The sacrifice worked. Now we can get from the temple to the other world. That stupid monk thinks he is such a friend of our group. He will be the next to go to the power. Who knows? Perhaps he\n",
                    "will turn into a wither skeleton, or a zombie. I don’t care, as long as he dies. was able to trick him into thinking we are a part of the light fighters. I cannot wait to see his happy face drop, and then bleed out\n",
                    " onto the altar. Soon, however, we will leave this for the other world, and so we must fill it. Fill it with traps made of blood. Solidifying blood is not hard when the helps you. Soon will rule the whole world, and I will be at his side! Now, to take care of that pesky guard at the fort in the other world"));
            builder.put(new ResourceLocation("shrines:shrine_of_savanna"), new StructureNovel(
                    "The sacred place is made. It took many moons to cleanse this hot place, but at last, I can talk yet again with my masters\n",
                    " It has been so long since I heard their voices. It is not easy to turn a desert into a forest. But, with my will and hard work, the sky has blessed me, and let me turn this barren wasteland into a lust place,",
                    "where all come and let all their troubles melt away. {Ink spill} Oh my, they want me back?",
                    "Oh happy day! I will leave and go home! I know that this place will help any weary travelers who find it by the gods will. If you read this, Feel free to take what you need"));
            builder.put(new ResourceLocation("shrines:mineral_temple"), new StructureNovel("The hideout is almost complete. ᒲ⊣ᔑリℸ ̣ ⍑ Is " +
                    "in desperate need of a hide out, and my own home would not suffice. Therefore, I am making this rock. She will be able to farm the moss, smear it with blood, and " +
                    "become fully invisible. Well, the house will be invisible. She will still be visible if she leaves. However, if we ca",
                    "N stop the progress of that nun and stupid monk, we may be able to win  ̵̿̚ ̶̉̍ ̷̔́ ̸́̍ ̸̛̑ ̴̔̊ ̶̿̈ ̸́̔ ̷̃́ ̴̽͂ ̵̍̅ ̷̾͠ ̶͒͠ ̴͐́ ̷̀̓ ̸̆̕ ̵̑̀ ̶̍͝ ̷̈́͊ ̴͑̚ ̸͑̌ ̸̀͋ ̶̊͝ " +
                            "̸̓͊", "Of course, the monk would not be stupid, like her. Perhaps… Perhaps making this monster is not the best idea. However, I cannot stop now. Someone must " +
                    "win this. And I will be the one to make the winning move, with that witches help"));
            builder.put(new ResourceLocation("shrines:small_temple"), new StructureNovel("I have found a new family in the forest. The " +
                    "woman is going to have a child. I must stay away from them. The last thing I need is for them to alert the gods where I am. It was an ingenious idea to hide in their " +
                    "own temple, but I cannot be found until They come.",
                    "The woman thinks this is my house. She is sick and going to die. That idiotic man thinks that I will help deliver her child. I have no choice. I must play along. I " +
                            "need a plan. I do have an idea. Please reply to me, ᔑ⎓⋮ᓭ⎓╎⋮∴ʖ╎⍊ʖ╎∴ʖ∷⍊∷∴ʖ⍊. I need your help.",
                    """
                            It has been done. The snowstorm spell you sent me worked perfectly. The man wonders where his wife is, but I will have him dead by tonight. As I promised, their souls will be yours. As of now, I plan to leave this place. Thank you for your help ᔑ⎓⋮ᓭ⎓╎⋮∴ʖ╎⍊ʖ╎∴ʖ∷⍊∷∴ʖ⍊.\s

                            Signed,
                            ᒲ⊣ᔑリℸ ̣ ⍑"""));
            builder.put(new ResourceLocation("shrines:water_shrine"), new StructureNovel("The spring has been found. We needed this water " +
                    "badly. We cannot keep burning our tinder just to make water. My wife thinks that this will be a good place to raise a baby, but I cannot help but miss the warmth of my desert home.",
                    "\nYou will need to search for further parts in other structures! Here is nothing left!"));
            builder.put(new ResourceLocation("shrines:player_house"), new StructureNovel("The house is somewhat finished. I can’t wait to " +
                    "get out of " +
                    "this hellish cold! I have been working nonstop on the roof, but every time I get close to sealing the roof, it collapses! Yet another problem with snow. My wife is " +
                    "going to have her baby soon, and so she is in a house nearby right now with an old lady. Hopefully I will be done with this house by next week.",
                    "I can barely lift my hand to write now. The blizzard and the fever have sapped all the life out of me. I am going to try and find my wife tomorrow. She disappeared, " +
                            "and the old lady said she went to meet me. I am not sure I trust this woman anymore. I- [blood and scribbles from here before]"));
            builder.put(new ResourceLocation("shrines:end_temple"), new StructureNovel(
                    "Tʜɘ ƨtoɿɒϱɘ dυnʞɘɿ ʜɒƨ dɘɘn ʇiniƨʜɘb. Tʜɘ ƨqibɘɿƨ ʜɒvɘ dɘɘn movɘb to tʜɘiɿ nɘw ʜomɘ. Tʜɘ ƨtɿibɘɿƨ ɒɿɘ ɒ dit tɿiɔʞiɘɿ, ɒƨ tʜɘγ will nɘɘb ɒ ɔonƨtɒnt ʜɘɒt ƨoυɿɔɘ, " +
                            "ɒnb tʜɘ monʞ bib not tɘll υƨ iʇ ɔoɿυƨ ƨtɘmƨ will dυɿn…",
                    "Tʜɘ lɒvɒ ʜɒƨ dɘɘn ƨυɔɔɘƨƨʇυllγ tɿɒnƨqoɿtɘb ʇoɿ tʜɘ nɘtʜɘɿ to tʜɘ ƨtoɿɒϱɘ dυnʞɘɿ. Iʇ tʜɘ ⍑̷͆̈ꖎ̴̄͐|̵͂̈|̷͘͠ ̸̈́̓!̴̛̏¡̷̃̊∷̷̽͠ᒷ̶̒́╎̷̇́ᓭ̶̚͘ℸ̷̅́ ̸͆̌ ̶̇́ᓭ̵̽̍ ɘvɘɿ ʜɒƨ to dɒɔʞ " +
                            "oʇʇ, wɘ will dɘ ɒdlɘ to ƨɘnb tʜɘm ʜɘlq ʇɿom",
                    "        ",
                    "I ɒm bγinϱ. Anγonɘ wʜo ʇinbƨ tʜiƨ, qlɘɒƨɘ, lɘt tʜɘ monʞ ʞnow… Tʜɒ wɘ tɿib ʜɒɿb ɒnbɘ \uD801\uDC12loob/ϱoɿɘ? On qɒqɘɿ"));
            return builder.build();
        });
    }

    @Override
    public @NotNull String getName() {
        return "Novels";
    }
}
