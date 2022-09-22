/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.core.util.reloadlisteners;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.core.util.reloadlisteners.PoppetColourManager.PoppetColour;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PoppetColourManager extends JsonReloadListener implements Supplier<HashMap<String, PoppetColour>> {

	private static final Gson GSON = new Gson();
	private HashMap<String, PoppetColour> poppetColours = new HashMap<>();

	public PoppetColourManager(String directory) {
		super(GSON, directory);
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsonMap, IResourceManager resourceManager, IProfiler profiler) {
		HashMap<String, PoppetColour> outputMap = new HashMap<>();

		jsonMap.forEach((resourceLocation, jsonElement) -> {
			try {
				JsonObject jsonObject = JSONUtils.convertToJsonObject(jsonElement, "poppet_colours");

				JsonArray itemStrings = JSONUtils.getAsJsonArray(jsonObject, "items");
				JsonArray primary = JSONUtils.getAsJsonArray(jsonObject, "primary");
				JsonArray secondary = JSONUtils.getAsJsonArray(jsonObject, "secondary");

				PoppetColour poppetColour = new PoppetColour(primary.get(0).getAsInt(), primary.get(1).getAsInt(), primary.get(2).getAsInt(),
						secondary.get(0).getAsInt(), secondary.get(1).getAsInt(), secondary.get(2).getAsInt());
				for(int i = 0; i < itemStrings.size(); i++) {
					outputMap.put(itemStrings.get(i).getAsString(), poppetColour);
				}

			} catch (IllegalArgumentException | JsonParseException jsonparseexception) {
				Enchanted.LOGGER.error("Parsing error loading poppet colours {}: {}", resourceLocation, jsonparseexception.getMessage());
			}
		});
		this.poppetColours = outputMap;
	}

	@Override
	public HashMap<String, PoppetColour> get() {
		return poppetColours;
	}

	public static class PoppetColour {
		public final int red;
		public final int green;
		public final int blue;
		public final int red1;
		public final int green1;
		public final int blue1;

		public PoppetColour(int red, int green, int blue, int red1, int green1, int blue1) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.red1 = red1;
			this.green1 = green1;
			this.blue1 = blue1;
		}
	}

}
