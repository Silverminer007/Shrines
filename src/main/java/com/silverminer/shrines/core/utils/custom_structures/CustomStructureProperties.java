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
package com.silverminer.shrines.core.utils.custom_structures;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.util.registry.DynamicRegistries;

/**
 * @author Silverminer
 *
 */
public class CustomStructureProperties extends PropertyManager<CustomStructureProperties> {
	protected static final Logger LOG = LogManager.getLogger(CustomStructureProperties.class);
	public final Path path;
	public final boolean autosave;
	public final boolean keep_bounds;
	public final int bound_color;
	public final boolean save_bounds;
	public final boolean use_experimental;

	/**
	 * @param properties
	 */
	public CustomStructureProperties(Properties p, Path path) {
		super(p);
		this.autosave = this.get("autosave", true);
		this.keep_bounds = this.get("keep_bounds", true);
		this.bound_color = this.get("bound_color", 0xffffff);
		this.save_bounds = this.get("save_bounds", true);
		this.use_experimental = this.get("use_experimental", false);
		this.path = path;
	}

	public void save() {
		this.store(this.path);
	}

	public void store(Path path) {
		try (OutputStream outputstream = Files.newOutputStream(path)) {
			net.minecraftforge.common.util.SortedProperties.store(this.cloneProperties(), outputstream,
					"Shrines custom structures properties");
		} catch (IOException ioexception) {
			LOG.error("Failed to store properties to file: " + path);
		}

	}

	@Override
	protected CustomStructureProperties reload(DynamicRegistries registry, Properties prop) {
		return new CustomStructureProperties(prop, this.path);
	}

	public static CustomStructureProperties load(Path p) {
		return new CustomStructureProperties(CustomStructureProperties.loadFromFile(p), p);
	}
}