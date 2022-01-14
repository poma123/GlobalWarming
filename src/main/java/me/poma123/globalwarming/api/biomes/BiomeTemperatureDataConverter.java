package me.poma123.globalwarming.api.biomes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun4.utils.biomes.BiomeDataConverter;

/**
 * This {@link BiomeDataConverter} converts {@link JsonElement} to {@link BiomeTemperature}
 * to be used in our {@link io.github.thebusybiscuit.slimefun4.utils.biomes.BiomeMap}
 *
 * @author poma123
 *
 */
public class BiomeTemperatureDataConverter implements BiomeDataConverter<BiomeTemperature> {

    @Override
    public BiomeTemperature convert(JsonElement jsonElement) {
        JsonObject obj = jsonElement.getAsJsonObject();
        double temperature = obj.get("temperature").getAsDouble();
        double maxTempDrop = obj.get("max-temp-drop-at-night").getAsDouble();

        return new BiomeTemperature(temperature, maxTempDrop);
    }
}
