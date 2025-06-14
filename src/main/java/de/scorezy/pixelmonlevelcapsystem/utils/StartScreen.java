package de.scorezy.pixelmonlevelcapsystem.utils;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StartScreen {

    private static final Logger LOGGER = LogManager.getLogger("LevelCapSystem");

    public static void printStartupBanner(MinecraftServer server) {
        String serverType = server.getServerModName();

        log("§a  _    ___ ___ ");
        log("§a | |  / __/ __|   §eLevelCapSystem v2.0");
        log("§a | |_| (__\\__ \\   §7Running on - §b" + serverType);
        log("§a |____\\___|___/   §7by Jxstn / Scorezy");
        log("");
        log("§byou need help§7? §ajoin §7my §9discord§7: §ehttps://discord.gg/RnDtKgq3pK");
        log("");
    }

    private static void log(String message) {
        LOGGER.info(message);
    }
}
