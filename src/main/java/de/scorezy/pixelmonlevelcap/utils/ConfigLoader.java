package de.scorezy.pixelmonlevelcap.utils;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigLoader {

    private static final String CONFIG_FILE_NAME = "levelcap.properties";
    private static final Properties properties = new Properties();
    private static final String[] DEFAULT_LEVELS = {"50", "20", "30", "40", "50", "60", "70", "100"};

    public static void loadConfig() {
        Path configDir = FMLPaths.CONFIGDIR.get();
        File configFile = new File(configDir.toFile(), CONFIG_FILE_NAME);

        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        }

        try (FileReader reader = new FileReader(configFile)) {
            properties.load(reader);
            properties.forEach((key, value) -> System.out.println("Loaded property: " + key + " = " + value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDefaultConfig(File configFile) {
        for (int i = 0; i < DEFAULT_LEVELS.length; i++) {
            properties.setProperty("badge.level." + i, DEFAULT_LEVELS[i]);
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            properties.store(writer, "Badge Level Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getBadgeLevel(int badgeCount) {
        String defaultLevel = DEFAULT_LEVELS[Math.min(badgeCount, DEFAULT_LEVELS.length - 1)];
        String levelStr = properties.getProperty("badge.level." + badgeCount, defaultLevel);
        return Integer.parseInt(levelStr);
    }
}