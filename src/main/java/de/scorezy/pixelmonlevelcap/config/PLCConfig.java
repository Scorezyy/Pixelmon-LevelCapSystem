package de.scorezy.pixelmonlevelcap.config;

import de.scorezy.pixelmonlevelcap.lib.Reference;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class PLCConfig {
    public static class Common {
        // General
        public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> cappedLevels;

        public Common(ForgeConfigSpec.Builder builder) {
            // Category: General
            builder.push("general");
            cappedLevels = builder
                    .comment("Listed levels will be capped based on the number of badges.")
                    .defineList("cappedLevels", Reference.DEFAULT_CAPPED_LEVELS, o -> o instanceof Integer);
            builder.pop();
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static void setup() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
    }
}
