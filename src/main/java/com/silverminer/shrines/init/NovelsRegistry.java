package com.silverminer.shrines.init;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.packages.datacontainer.StructureNovel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class NovelsRegistry {
   public static final DeferredRegister<StructureNovel> NOVELS = DeferredRegister.create(StructureNovel.class, ShrinesMod.MODID);
   public static final Supplier<IForgeRegistry<StructureNovel>> NOVELS_REGISTRY = NOVELS.makeRegistry("shrines_structure_novels", RegistryBuilder::new);
   public static final RegistryObject<StructureNovel> HIGH_TEMPLE = NOVELS.register("high_temple", () -> new StructureNovel(
         "We have finally made the altar. It is wonderful. I know that they will love it. So will the queen. If we can get more help from them, then we can finally beat “Them”." +
               " Who do they think they are, just breaking off and stealing all of our possessions? Luckily, the queen shared with us, so we are not dead. However…",
         "the strange coral we found to make the lights is, how do I put it? Quite painful to get. We have to kill all these strange monsters to get it. Oh well, see you tomorrow diary!",
         "Oh my! The monsters! We woke up to screaming in the night. Apparently, jon tried to harvest some of the crystals for the queen. Oh, it gets me so mad! He should like ME, not her. But that's not important. What is important is that",
         """
               The monsters cursed us! The whole sea jumped up and swept the crops away! And now the land is cold. There is strange white stuff all over, and I am too cold to go outside. I do hope that the guards can protect us from the raids… Good night. I will write tomorrow.

               Oh. I
               have had a dream. It was from the leader of the betrayers! He said that I should join him. I had burst into laughter, but then he said that… He said he knew how to get jon to like me…
               
               How I wish it were true. He had said that If I go to the alter and break the seal, then the cold will stay, but he will enchant jon to love me…"""));
   public static final RegistryObject<StructureNovel> NETHER_PYRAMID = NOVELS.register("nether_pyramid", () -> new StructureNovel(
         "It has been done. The sacrifice worked. Now we can get from the temple to the other world. That stupid monk thinks he is such a friend of our group. He will be the next to go to the power. Who knows? Perhaps he\n",
         "will turn into a wither skeleton, or a zombie. I don’t care, as long as he dies. was able to trick him into thinking we are a part of the light fighters. I cannot wait to see his happy face drop, and then bleed out\n",
         " onto the altar. Soon, however, we will leave this for the other world, and so we must fill it. Fill it with traps made of blood. Solidifying blood is not hard when the helps you. Soon will rule the whole world, and I will be at his side! Now, to take care of that pesky guard at the fort in the other world"));
   public static final RegistryObject<StructureNovel> SHRINE_OF_SAVANNA = NOVELS.register("shrine_of_savanna", () -> new StructureNovel(
         "The sacred place is made. It took many moons to cleanse this hot place, but at last, I can talk yet again with my masters\n",
         " It has been so long since I heard their voices. It is not easy to turn a desert into a forest. But, with my will and hard work, the sky has blessed me, and let me turn this barren wasteland into a lust place,",
         "where all come and let all their troubles melt away. {Ink spill} Oh my, they want me back?",
         "Oh happy day! I will leave and go home! I know that this place will help any weary travelers who find it by the gods will. If you read this, Feel free to take what you need"));
}
