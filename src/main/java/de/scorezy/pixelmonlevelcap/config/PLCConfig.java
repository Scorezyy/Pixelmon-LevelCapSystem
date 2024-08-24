package de.scorezy.pixelmonlevelcap.config;

import de.scorezy.pixelmonlevelcap.lib.Reference;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class PLCConfig {
    /**
     * Common configs
     */
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

    /**
     * Client-only configs (if needed)
     */
    public static class Client {
        public Client(ForgeConfigSpec.Builder builder) {
        }
    }

    /**
     * Server-only configs (if needed)
     */
    public static class Server {
        public Server(ForgeConfigSpec.Builder builder) {
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

//    public static final Client CLIENT;
//    public static final ForgeConfigSpec CLIENT_SPEC;
//
//    public static final Server SERVER;
//    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();

//        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
//        CLIENT = clientSpecPair.getLeft();
//        CLIENT_SPEC = clientSpecPair.getRight();
//
//        final Pair<Server, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
//        SERVER = serverSpecPair.getLeft();
//        SERVER_SPEC = serverSpecPair.getRight();
    }

    public static void setup() {
        ModLoadingContext context = ModLoadingContext.get();
        context.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
//        context.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
//        context.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }
}
