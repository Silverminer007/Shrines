package com.silverminer.shrines.structures.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.config.StructureConfig.StructureGenConfig;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.AbstractStructureStart;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class CustomStructure extends AbstractStructure<NoFeatureConfig> {
	protected static final Logger LOG = LogManager.getLogger(CustomStructure.class);
	private int distance = 45;
	private int seperation = 12;
	private int seed = -1;
	private double spawn_chance = 0.6D;
	private boolean needGround = true;
	private boolean generate = true;
	private boolean useRandomVarianting = true;
	private ArrayList<Biome.Category> categories = new ArrayList<Biome.Category>();
	private ArrayList<String> blacklist = new ArrayList<String>();
	private HashMap<String, BlockPos> NAME_AND_OFFSET_PIECES = new HashMap<String, BlockPos>();

	public CustomStructure(Codec<NoFeatureConfig> codec, String name, List<String> config) {
		super(codec, 3, name);
		try {
			LOG.info("Constructing custom structure with config: {}", config);

			for (int i = 0; i < config.size(); i++) {
				String c = config.get(i);
				if (c.startsWith("#")) {
					continue;
				} else if (c.startsWith("Seed:")) {
					seed = Integer.valueOf(c.replace("Seed:", "").replaceAll(" ", ""));
				} else if (c.startsWith("Generate:")) {
					generate = Boolean.valueOf(c.replace("Generate:", "").replaceAll(" ", ""));
				} else if (c.startsWith("Spawn Chance:")) {
					spawn_chance = Double.valueOf(c.replace("Spawn Chance:", "").replaceAll(" ", ""));
				} else if (c.startsWith("Needs Ground:")) {
					needGround = Boolean.valueOf(c.replace("Needs Ground:", "").replaceAll(" ", ""));
				} else if (c.startsWith("Distance:")) {
					distance = Integer.valueOf(c.replace("Distance:", "").replaceAll(" ", ""));
				} else if (c.startsWith("Seperation:")) {
					seperation = Integer.valueOf(c.replace("Seperation:", "").replaceAll(" ", ""));
				} else if (c.startsWith("Use Random Varianting:")) {
					useRandomVarianting = Boolean.valueOf(c.replace("Use Random Varianting:", "").replaceAll(" ", ""));
				} else if (c.startsWith("Biome Categories:")) {
					String newS = c.replace("Biome Categories:", "").replaceAll(" ", "");
					if (newS.isEmpty()) {
						continue;
					}
					newS = newS.substring(1, newS.length() - 1);
					do {
						int index = newS.length();
						if (newS.contains(",")) {
							index = newS.indexOf(",");
						}
						String item = newS.substring(0, index);
						if (item.equals("ALL")) {
							for (Category cat : Biome.Category.values()) {
								categories.add(cat);
							}
							break;
						} else if (item.equals("DEFAULT")) {
							categories.add(Biome.Category.PLAINS);
							categories.add(Biome.Category.TAIGA);
							categories.add(Biome.Category.FOREST);
							break;
						} else {
							Biome.Category v = Biome.Category.valueOf(item);
							if (v != null)
								categories.add(v);
						}
						newS = newS.substring(index);
					} while (!newS.isEmpty());
				} else if (c.startsWith("Biome Blacklist:")) {
					String newS = c.replace("Biome Blacklist:", "").replaceAll(" ", "");
					if (newS.isEmpty()) {
						continue;
					}
					newS = newS.substring(1, newS.length() - 1);
					do {
						int index = newS.length();
						if (newS.contains(",")) {
							index = newS.indexOf(",");
						}
						String item = newS.substring(0, index);
						if (!item.isEmpty())
							blacklist.add(item);
						newS = newS.substring(index);
					} while (!newS.isEmpty());
				} else if (c.startsWith("Pieces:")) {
					String line;
					do {
						line = config.get(++i).replace(" ", "");
						String path = line.substring(line.indexOf("[") + 1, line.indexOf(","));
						String p2 = line.substring(line.indexOf(","));
						String pos = p2.substring(p2.indexOf("[") + 1, p2.indexOf("]"));
						try {
							int x = Integer.valueOf(pos.substring(0, pos.indexOf(",")));
							int y = Integer.valueOf(pos.substring(pos.indexOf(",") + 1, pos.lastIndexOf(",")));
							int z = Integer.valueOf(pos.substring(pos.lastIndexOf(",") + 1));
							BlockPos offset = new BlockPos(x, y, z);
							NAME_AND_OFFSET_PIECES.put(path, offset);
						} catch (NumberFormatException e) {
							LOGGER.error("Failed to parse offset pos of \"{}\" at path \"{}\", \"{}\"", name, path,
									pos);
						}
					} while (line.endsWith(","));
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (seed == -1) {
			seed = new Random().nextInt(Integer.MAX_VALUE);
		}
	}

	public boolean isEndStructure() {
		return this.categories.contains(Biome.Category.THEEND);
	}

	public boolean isNetherStructure() {
		return this.categories.contains(Biome.Category.NETHER);
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
		return CustomStructure.Start::new;
	}

	@Override
	public StructureGenConfig getConfig() {
		return Config.STRUCTURES.CUSTOM;
	}

	public int getDistance() {
		return this.distance;
	}

	public int getSeparation() {
		return this.seperation;
	}

	public int getSeedModifier() {
		return this.seed;
	}

	public double getSpawnChance() {
		return this.spawn_chance;
	}

	public boolean needsGround() {
		return this.needGround;
	}

	/**
	 * 
	 * @param name
	 * @param category
	 * @return
	 */
	public boolean validateSpawn(ResourceLocation name, Category category) {
		if (!generate) {
			return false;
		}
		boolean flag = categories.contains(category);

		if (!blacklist.isEmpty() && flag) {
			flag = !blacklist.contains(name.toString());
		}

		return flag;
	}

	public static class Start extends AbstractStructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
				int p_i225806_5_, long seed) {
			super(structure, chunkX, chunkZ, boundingbox, p_i225806_5_, seed);
		}

		@Override
		public void generatePieces(DynamicRegistries registries, ChunkGenerator chunkGenerator,
				TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			int i = chunkX * 16;
			int j = chunkZ * 16;
			BlockPos blockpos = new BlockPos(i, 0, j);
			Rotation rotation = Rotation.getRandom(this.random);
			if (!(this.getFeature() instanceof CustomStructure))
				return;
			CustomStructure cS = (CustomStructure) this.getFeature();
			CustomPiece.generate(templateManager, blockpos, rotation, this.pieces, this.random, cS.useRandomVarianting,
					cS.NAME_AND_OFFSET_PIECES, cS.name);
			this.calculateBoundingBox();
		}
	}
}