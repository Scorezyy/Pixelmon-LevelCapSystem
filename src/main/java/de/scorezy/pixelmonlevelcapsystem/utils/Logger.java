package de.scorezy.pixelmonlevelcapsystem.utils;

import org.apache.logging.log4j.LogManager;

public class Logger {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger("LevelCapSystem");

    public static void debug(String message) {
        if (ConfigLoader.getSettingsConfig().isDebug()) {
            LOGGER.info("§8[§bLCS§8]§e-> §f" + message);
        }
    }

}
