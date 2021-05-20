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
package com.silverminer.shrines.core.structures.custom.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ResourceData {
	protected static final Logger LOGGER = LogManager.getLogger(ResourceData.class);
	private MutableBoundingBox bounds;
	private BlockPos offset;
	private String name;
	private RegistryKey<World> dimension;

	public ResourceData(String name, MutableBoundingBox bound, RegistryKey<World> dimension) {
		this(name, bound, dimension, BlockPos.ZERO);
	}

	public ResourceData(String name, MutableBoundingBox bound, RegistryKey<World> dimension, BlockPos offset) {
		this.bounds = bound;
		this.setName(name);
		this.offset = offset;
		this.setDimension(dimension);
	}

	public BlockPos getOffset() {
		return offset;
	}

	public void setOffset(BlockPos offset) {
		this.offset = offset;
	}

	public MutableBoundingBox getBounds() {
		return bounds;
	}

	public void setBounds(MutableBoundingBox bounds) {
		this.bounds = bounds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "ResourceData[Name:" + this.getName() + ",Bounds:" + this.getBounds().toString() + ",Offset:"
				+ this.getOffset().asLong() + "]";
	}

	public CompoundNBT save() {
		CompoundNBT resource = new CompoundNBT();
		resource.putString("name", this.getName());
		resource.put("bound", this.getBounds().createTag());
		resource.putLong("offset", this.getOffset().asLong());
		resource.putString("dimension",
				(dimension != null && dimension.location() != null && dimension.location().toString() != null)
						? dimension.location().toString()
						: "");
		return resource;
	}

	public static ResourceData load(CompoundNBT nbt) {
		RegistryKey<World> dimension;
		if (nbt.contains("dimension", 8)) {
			ResourceLocation dimensionRegistryKey = ResourceLocation.tryParse(nbt.getString("dimension"));
			dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, dimensionRegistryKey);
		} else {
			dimension = World.OVERWORLD;
		}
		return new ResourceData(nbt.getString("name"), new MutableBoundingBox(nbt.getIntArray("bound")), dimension,
				BlockPos.of(nbt.getLong("offset")));
	}

	public RegistryKey<World> getDimension() {
		return dimension;
	}

	public void setDimension(RegistryKey<World> dimension) {
		this.dimension = dimension;
	}
}