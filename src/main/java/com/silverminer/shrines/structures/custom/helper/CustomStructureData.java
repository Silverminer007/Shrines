/**
 * Silverminer (and Team)
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * 
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.structures.custom.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.utils.custom_structures.ModTemplateManager;
import com.silverminer.shrines.utils.custom_structures.OptionParsingResult;
import com.silverminer.shrines.utils.custom_structures.Utils;
import com.silverminer.shrines.utils.network.CustomStructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

public class CustomStructureData {
	protected static final Logger LOGGER = LogManager.getLogger(CustomStructureData.class);
	public static final List<String> OPTIONS = Lists.newArrayList();

	public String name;

	public final List<ConfigOption<?>> CONFIGS = Lists.newArrayList();
	public ConfigOption<Boolean> generate = add(new ConfigOption<Boolean>("generate", true, Boolean::valueOf,
			BoolArgumentType.bool(), BoolArgumentType::getBool));
	public ConfigOption<Double> spawn_chance = add(new ConfigOption<Double>("spawn_chance", 0.6D, Double::valueOf,
			DoubleArgumentType.doubleArg(0.0, 1.0), DoubleArgumentType::getDouble));
	public ConfigOption<Boolean> needs_ground = add(new ConfigOption<Boolean>("needs_ground", true, Boolean::valueOf,
			BoolArgumentType.bool(), BoolArgumentType::getBool));
	public ConfigOption<Boolean> use_random_varianting = add(new ConfigOption<Boolean>("use_random_varianting", true,
			Boolean::valueOf, BoolArgumentType.bool(), BoolArgumentType::getBool));
	public ConfigOption<Integer> distance = add(new ConfigOption<Integer>("distance", 50, Integer::valueOf,
			IntegerArgumentType.integer(), IntegerArgumentType::getInteger));
	public ConfigOption<Integer> seed = add(new ConfigOption<Integer>("seed", 0, Integer::valueOf,
			IntegerArgumentType.integer(0), IntegerArgumentType::getInteger));
	public ConfigOption<Integer> seperation = add(new ConfigOption<Integer>("seperation", 8, Integer::valueOf,
			IntegerArgumentType.integer(), IntegerArgumentType::getInteger));
	public ConfigOption<List<Biome.Category>> categories = add(new ConfigOption<List<Biome.Category>>("categories",
			Lists.newArrayList(Biome.Category.PLAINS, Biome.Category.TAIGA, Biome.Category.FOREST),
			CustomStructureData::readCategories, StringArgumentType.greedyString(), StringArgumentType::getString,
			false));
	public ConfigOption<List<String>> blacklist = add(
			new ConfigOption<List<String>>("blacklist", Lists.newArrayList(), CustomStructureData::readBlackList,
					StringArgumentType.greedyString(), StringArgumentType::getString, false));
	public ConfigOption<List<PieceData>> pieces = add(new ConfigOption<List<PieceData>>("pieces",
			Lists.newArrayList(new PieceData("resource", BlockPos.ZERO)), CustomStructureData::readPieces,
			StringArgumentType.greedyString(), StringArgumentType::getString, false));
	public ConfigOption<Boolean> ignore_air = add(new ConfigOption<Boolean>("ignore_air", true, Boolean::valueOf,
			BoolArgumentType.bool(), BoolArgumentType::getBool));
	public ConfigOption<Integer> base_height_offset = add(new ConfigOption<Integer>("base_height_offset", 0, Integer::valueOf,
			IntegerArgumentType.integer(), IntegerArgumentType::getInteger));
	public final ArrayList<ResourceData> PIECES_ON_FLY = Lists.newArrayList();

	public CustomStructureData(String name, Random rand) {
		this(name, rand.nextInt(Integer.MAX_VALUE));
	}

	public CustomStructureData(String name, int seed) {
		this.name = name;
		this.seed.setValue(seed);
	}

	private CustomStructureData() {
		this("", 0);
	}

	public <T> ConfigOption<T> add(ConfigOption<T> option) {
		if (!OPTIONS.contains(option.getName()) && option.getUseInCommand()) {
			OPTIONS.add(option.getName());
		}
		if (!CONFIGS.contains(option)) {
			CONFIGS.add(option);
		}
		return option;
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		String config = "";
		for (ConfigOption<?> co : CONFIGS) {
			config += co.toString() + ";";
		}
		return config;
	}

	public String toStringReadAble() {
		return this.toString().replaceAll(";", ";\n");
	}

	public boolean calculateBounds(BlockPos c1, BlockPos c2) {
		PIECES_ON_FLY.clear();
		if (c1.getX() > c2.getX()) {
			int x = c1.getX();
			c1 = new BlockPos(c2.getX(), c1.getY(), c1.getZ());
			c2 = new BlockPos(x, c2.getY(), c2.getZ());
		}
		if (c1.getY() > c2.getY()) {
			int y = c1.getY();
			c1 = new BlockPos(c1.getX(), c2.getY(), c1.getZ());
			c2 = new BlockPos(c2.getX(), y, c2.getZ());
		}
		if (c1.getZ() > c2.getZ()) {
			int z = c1.getZ();
			c1 = new BlockPos(c1.getX(), c1.getY(), c2.getZ());
			c2 = new BlockPos(c2.getX(), c2.getY(), z);
		}
		int sizeX = c2.getX() - c1.getX();
		int sizeY = c2.getY() - c1.getY();
		int sizeZ = c2.getZ() - c1.getZ();
		int lastX = sizeX % 48;
		int countX = (sizeX - lastX) / 48;
		int lastY = sizeY % 48;
		int countY = (sizeY - lastY) / 48;
		int lastZ = sizeZ % 48;
		int countZ = (sizeZ - lastZ) / 48;
		int i = 0;
		for (int x = 0; x <= countX; x++) {
			for (int y = 0; y <= countY; y++) {
				for (int z = 0; z <= countZ; z++) {
					BlockPos corner1 = new BlockPos(x * 48, y * 48, z * 48);
					int nX = 48;
					int nY = 48;
					int nZ = 48;
					if (x == countX) {
						nX = lastX;
					}
					if (y == countY) {
						nY = lastY;
					}
					if (z == countZ) {
						nZ = lastZ;
					}
					BlockPos corner2 = corner1.offset(nX, nY, nZ);
					if (corner2.getX() == 0 || corner2.getY() == 0 || corner2.getZ() == 0) {
						continue;
					}
					corner1 = corner1.offset(c1);
					corner2 = corner2.offset(c1);
					PIECES_ON_FLY.add(new ResourceData(
							this.name + i++, MutableBoundingBox.createProper(corner1.getX(), corner1.getY(),
									corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ()),
							corner1.subtract(c1)));
				}
			}
		}
		if (this.PIECES_ON_FLY.isEmpty()) {
			return false;
		}
		return true;
	}

	public static CompoundNBT write(CustomStructureData csd) {
		CompoundNBT tag = new CompoundNBT();
		tag.putString("config", csd.toString());
		tag.putString("name", csd.name);
		tag.putInt("bounds", csd.PIECES_ON_FLY.size());
		int i = 0;
		for (ResourceData rd : csd.PIECES_ON_FLY) {
			tag.putString("name" + i, rd.getName());
			tag.put("bounds" + i, rd.getBounds().createTag());
			i++;
		}
		return tag;
	}

	public static CustomStructureData read(CompoundNBT tag) {
		CustomStructureData csd = new CustomStructureData();
		if (tag == null) {
			LOGGER.error("ERROR: Server sended empty structure data");
			return csd;
		}
		csd.fromString(tag.getString("config"));
		csd.name = tag.getString("name");
		int bounds = tag.getInt("bounds");
		csd.PIECES_ON_FLY.clear();
		for (int i = 0; i < bounds; i++) {
			csd.PIECES_ON_FLY.add(new ResourceData(tag.getString("name" + i),
					new MutableBoundingBox(((IntArrayNBT) tag.get("bounds" + i)).getAsIntArray())));
		}
		return csd;
	}

	public static void sendToClients() {
		ShrinesPacketHandler.sendToAll(toPacket());
	}

	public static void sendToClient(PlayerEntity pe) {
		ShrinesPacketHandler.sendTo(toPacket(), pe);
	}

	public static CustomStructuresPacket toPacket() {
		return new CustomStructuresPacket(Utils.customsStructs);
	}

	public boolean savePieces(ServerWorld serverWorld, MinecraftServer server, String author, boolean includeEntities) {
		ModTemplateManager templatemanager;
		try {
			templatemanager = new ModTemplateManager(Utils.getLocationOf("").getCanonicalFile().toPath(),
					server.getFixerUpper());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		for (ResourceData rd : PIECES_ON_FLY) {
			ResourceLocation location = new ResourceLocation(Shrines.MODID, name + "/" + rd.getName());
			MutableBoundingBox mbb = rd.getBounds();
			BlockPos c0 = new BlockPos(mbb.x0, mbb.y0, mbb.z0);
			BlockPos c1 = new BlockPos(mbb.x1, mbb.y1, mbb.z1);

			Template template;
			try {
				template = templatemanager.getOrCreate(location);
			} catch (ResourceLocationException resourcelocationexception) {
				LOGGER.error(resourcelocationexception);
				return false;
			}

			template.fillFromWorld(serverWorld, c0, c1.subtract(c0), includeEntities, Blocks.STRUCTURE_VOID);
			template.setAuthor(author);
			try {
				if (!templatemanager.save(location)) {
					return false;
				}
			} catch (ResourceLocationException resourcelocationexception) {
				LOGGER.error(resourcelocationexception);
				return false;
			}
		}
		return true;
	}

	public boolean loadPieces(ServerWorld serverWorld, MinecraftServer server, BlockPos loadPos, Rotation rot) {
		ModTemplateManager templatemanager;
		for (PieceData pd : this.pieces.getValue()) {
			ResourceLocation location = new ResourceLocation(Shrines.MODID, name + "/" + pd.path);
			try {
				templatemanager = new ModTemplateManager(Utils.getLocationOf("").getCanonicalFile().toPath(),
						server.getFixerUpper());
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

			Template template;
			try {
				template = templatemanager.get(location);
			} catch (ResourceLocationException resourcelocationexception) {
				return false;
			}

			if (template == null)
				return false;
			BlockPos blockpos = loadPos.offset(pd.offset);

			PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(rot)
					.setIgnoreEntities(false).setChunkPos((ChunkPos) null);

			template.placeInWorldChunk(serverWorld, blockpos, placementsettings, createRandom(this.seed.getValue()));
		}
		return true;
	}

	private static Random createRandom(long seed) {
		return seed == 0L ? new Random(Util.getMillis()) : new Random(seed);
	}

	public void addBounds() {
		List<PieceData> pds = Lists.newArrayList();
		for (ResourceData rd : PIECES_ON_FLY) {
			PieceData pd = new PieceData(rd.getName(), rd.getOffset());
			pds.add(pd);
		}
		this.pieces.setValue(pds);
		if (!Utils.properties.keep_bounds)
			this.PIECES_ON_FLY.clear();
	}

	public void fromString(String config) {
		config = config.replaceAll(" ", "").replaceAll("\n", "");
		while (true) {
			int idx = config.indexOf(";");
			if (idx == -1) {
				break;
			}
			String sub = config.substring(0, idx);
			int idx2 = sub.indexOf(":");
			if (idx2 == -1) {
				break;
			}
			this.fromString(sub.substring(0, idx2), sub.substring(idx2 + 1));
			if (config.length() <= idx + 1) {
				break;
			}
			config = config.substring(idx + 1);
		}
	}

	public OptionParsingResult fromString(String option, String value) {
		for (ConfigOption<?> co : CONFIGS) {
			if (co.getName().equals(option)) {
				OptionParsingResult res = co.fromString(value, this);
				return res;
			}
		}
		return new OptionParsingResult(false, null);
	}

	public static List<Biome.Category> readCategories(String s) {
		if (s.startsWith("[") && s.endsWith("]")) {
			s = s.substring(1, s.length() - 1);
		}
		try {
			List<String> cats = Lists.newArrayList();
			while ((s.contains(","))) {
				int idx = s.lastIndexOf(",");
				cats.add(s.substring(idx + 1));
				s = s.substring(0, idx);
			}
			cats.add(s);
			List<Biome.Category> categories = Lists.newArrayList();
			for (String cat : cats) {
				if (cat.equals("DEFAULT")) {
					return Lists.newArrayList(Biome.Category.PLAINS, Biome.Category.TAIGA, Biome.Category.FOREST);
				} else if (cat.equals("ALL")) {
					return Lists.newArrayList(Biome.Category.values());
				} else {
					Biome.Category c = Biome.Category.valueOf(cat);
					if (c != null) {
						categories.add(c);
					}
				}
			}
			return categories;
		} catch (Throwable t) {
			LOGGER.warn("Failed to parse [{}] to Categories", s);
			return null;
		}
	}

	public static List<String> readBlackList(String s) {
		if (s.startsWith("[") && s.endsWith("]")) {
			s = s.substring(1, s.length() - 1);
		}
		List<String> list = Lists.newArrayList();
		while ((s.contains(","))) {
			int idx = s.lastIndexOf(",");
			String s1 = s.substring(idx + 1);
			if (!validateBiome(s1)) {
				return null;
			}
			list.add(s1);
			s = s.substring(0, idx);
		}
		if (validateBiome(s))
			list.add(s);
		else
			return null;
		return list;
	}

	public static boolean validateBiome(String s) {
		for (ResourceLocation b : ForgeRegistries.BIOMES.getKeys()) {
			if (b.toString().equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static List<PieceData> readPieces(String s) {
		if (s.startsWith("[") && s.endsWith("]")) {
			s = s.substring(1, s.length() - 1);
		}
		List<String> cats = Lists.newArrayList();
		String[] parts = s.split(",");
		if (!(parts.length % 4 == 0)) {
			LOGGER.info("Something went wrong reading pieces: Comma count didn't match");
			return null;
		}
		for (int i = 0; i < parts.length / 4; i++) {
			int idx = i * 4;
			cats.add(parts[idx]  + "," + parts[idx + 1] + "," + parts[idx + 2] + "," + parts[idx + 3]);
		}
		/*
		 * while (s.contains("+")) { int idx = s.lastIndexOf("+");
		 * cats.add(s.substring(idx + 1)); s = s.substring(0, idx); } cats.add(s);
		 */
		List<PieceData> categories = Lists.newArrayList();
		for (String cat : cats) {
			PieceData c = PieceData.fromString(cat);
			if (c != null) {
				categories.add(c);
			}
		}
		return categories;
	}
}