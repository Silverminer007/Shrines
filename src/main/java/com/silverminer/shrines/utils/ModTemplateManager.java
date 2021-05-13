package com.silverminer.shrines.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.FileUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.datafix.DefaultTypeReferences;
import net.minecraft.world.gen.feature.template.Template;

public class ModTemplateManager {
	private static final Logger LOGGER = LogManager.getLogger();
	private final Map<ResourceLocation, Template> structureRepository = Maps.newHashMap();
	private final DataFixer fixerUpper;
	private final Path generatedDir;

	public ModTemplateManager(Path path, DataFixer p_i232119_3_) {
		this.fixerUpper = p_i232119_3_;
		this.generatedDir = path.normalize();
		LOGGER.info("Constructing mod template manager with file: {}", this.generatedDir);
	}

	public Template getOrCreate(ResourceLocation location) {
		Template template = this.get(location);
		if (template == null) {
			template = new Template();
			this.structureRepository.put(location, template);
		}
		return template;
	}

	@Nullable
	public Template get(ResourceLocation resourcelocation) {
		return this.structureRepository.computeIfAbsent(resourcelocation, (p_209204_1_) -> {
			Template template = this.loadFromGenerated(p_209204_1_);
			return template;
		});
	}

	@Nullable
	private Template loadFromGenerated(ResourceLocation p_195428_1_) {
		if (!this.generatedDir.toFile().isDirectory()) {
			return null;
		} else {
			Path path = this.createAndValidatePathToStructure(p_195428_1_, ".nbt");

			LOGGER.debug("Loading custom structure piece from {}", path);
			try (InputStream inputstream = new FileInputStream(path.toFile())) {
				return this.readStructure(inputstream);
			} catch (FileNotFoundException filenotfoundexception) {
				LOGGER.warn("Couldn't load structure from {}, because of {}", path, filenotfoundexception);
				return null;
			} catch (IOException ioexception) {
				LOGGER.error("Couldn't load structure from {}", path, ioexception);
				return null;
			}
		}
	}

	private Template readStructure(InputStream p_209205_1_) throws IOException {
		CompoundNBT compoundnbt = CompressedStreamTools.readCompressed(p_209205_1_);
		return this.readStructure(compoundnbt);
	}

	public Template readStructure(CompoundNBT p_227458_1_) {
		if (!p_227458_1_.contains("DataVersion", 99)) {
			p_227458_1_.putInt("DataVersion", 500);
		}

		Template template = new Template();
		template.load(NBTUtil.update(this.fixerUpper, DefaultTypeReferences.STRUCTURE, p_227458_1_,
				p_227458_1_.getInt("DataVersion")));
		return template;
	}

	public Path createPathToStructure(ResourceLocation p_209509_1_, String p_209509_2_) {
		try {
			Path path = this.generatedDir.resolve(p_209509_1_.getNamespace());
			return FileUtil.createPathToResource(path, p_209509_1_.getPath(), p_209509_2_);
		} catch (InvalidPathException invalidpathexception) {
			throw new ResourceLocationException("Invalid resource path: " + p_209509_1_, invalidpathexception);
		}
	}

	private Path createAndValidatePathToStructure(ResourceLocation p_209510_1_, String p_209510_2_) {
		if (p_209510_1_.getPath().contains("//")) {
			throw new ResourceLocationException("Invalid resource path: " + p_209510_1_);
		} else {
			Path path = this.createPathToStructure(p_209510_1_, p_209510_2_);
			if (path.startsWith(this.generatedDir) && FileUtil.isPathNormalized(path)
					&& FileUtil.isPathPortable(path)) {
				return path;
			} else {
				throw new ResourceLocationException("Invalid resource path: " + path);
			}
		}
	}

	public boolean save(ResourceLocation location) {
		Template template = this.structureRepository.get(location);
		if (template == null) {
			return false;
		} else {
			Path path = this.createAndValidatePathToStructure(location, ".nbt");
			Path path1 = path.getParent();
			if (path1 == null) {
				return false;
			} else {
				try {
					Files.createDirectories(Files.exists(path1) ? path1.toRealPath() : path1);
				} catch (IOException ioexception) {
					LOGGER.error("Failed to create parent directory: {}", (Object) path1);
					return false;
				}

				CompoundNBT compoundnbt = template.save(new CompoundNBT());

				try (OutputStream outputstream = new FileOutputStream(path.toFile())) {
					CompressedStreamTools.writeCompressed(compoundnbt, outputstream);
					return true;
				} catch (Throwable throwable) {
					return false;
				}
			}
		}
	}

	public void remove(ResourceLocation p_189941_1_) {
		this.structureRepository.remove(p_189941_1_);
	}
}