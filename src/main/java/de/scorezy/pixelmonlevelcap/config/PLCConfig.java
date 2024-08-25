package de.scorezy.pixelmonlevelcap.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import de.scorezy.pixelmonlevelcap.lib.Reference;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.Arrays;
import java.util.List;

@ConfigPath(Reference.CONFIG_PATH)
@ConfigSerializable
public class PLCConfig extends AbstractYamlConfig {
    /**
     * These are the default values that will be used if this entry not created.
     */
    private List<Integer> cappedLevels = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 100);

    public List<Integer> getCappedLevels() {
        return cappedLevels;
    }
}
