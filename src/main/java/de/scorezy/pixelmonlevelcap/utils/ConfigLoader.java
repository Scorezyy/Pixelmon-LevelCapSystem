package de.scorezy.pixelmonlevelcap.utils;

import info.pixelmon.repack.yaml.snakeyaml.DumperOptions;
import info.pixelmon.repack.yaml.snakeyaml.Yaml;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class ConfigLoader {

    private static final String CONFIG_FILE_NAME = "levelcap.yml";
    private static final String[] DEFAULT_LEVELS = {"10", "20", "30", "40", "50", "60", "70", "80", "100"};

    private static final String DEFAULT_LEVEL_BLOCKED_MESSAGE = "&cThis level is too high for your Pokémon!";
    private static final String DEFAULT_MAX_LEVEL_REACHED_MESSAGE = "&cYour &ePokémon &chas already reached the maximum level";
    private static final String DEFAULT_CAPTURE_BLOCKED_MESSAGE = "&cThis &ePokémon &cis too strong for you to catch!";
    private static final String DEFAULT_INTERACT_BLOCKED_MESSAGE = "&cThe Pokémon is too high level for you!";
    private static final String DEFAULT_TRADE_BLOCKED_MESSAGE = "&cYou cannot trade your Pokémon because it exceeds the allowed level!";
    private static final String DEFAULT_NPCTRADE_ACCESS_MESSAGE = "&aThe level of the traded Pokémon has been adjusted to match your badge level!";
    private static final String DEFAULT_CONFIG_LOADED = "&aConfig has been successfully reloaded!";

    private static final Map<String, Object> configData = new HashMap<>();

    public static void loadConfig() {
        Path configDir = FMLPaths.CONFIGDIR.get();
        File configFile = new File(configDir.toFile(), CONFIG_FILE_NAME);

        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        }

        try (BufferedReader reader = Files.newBufferedReader(configFile.toPath(), StandardCharsets.UTF_8)) {
            Yaml yaml = new Yaml();
            configData.putAll(yaml.load(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDefaultConfig(File configFile) {
        Map<String, Object> yamlData = new HashMap<>();

        yamlData.put("badge_levels", createBadgeLevels());
        yamlData.put("messages", createMessages());

        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        Yaml yaml = new Yaml(options);

        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(configFile.toPath()), StandardCharsets.UTF_8)) {
            yaml.dump(yamlData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> createBadgeLevels() {
        Map<String, String> badgeLevels = new HashMap<>();
        for (int i = 0; i < DEFAULT_LEVELS.length; i++) {
            badgeLevels.put("badge.level." + i, DEFAULT_LEVELS[i]);
        }
        return badgeLevels;
    }

    private static Map<String, String> createMessages() {
        Map<String, String> messages = new HashMap<>();
        messages.put("interact_blocked", DEFAULT_INTERACT_BLOCKED_MESSAGE);
        messages.put("capture_blocked", DEFAULT_CAPTURE_BLOCKED_MESSAGE);
        messages.put("level_blocked", DEFAULT_LEVEL_BLOCKED_MESSAGE);
        messages.put("max_level_reached", DEFAULT_MAX_LEVEL_REACHED_MESSAGE);
        messages.put("trade_blocked", DEFAULT_TRADE_BLOCKED_MESSAGE);
        messages.put("npcTrade_access_message", DEFAULT_NPCTRADE_ACCESS_MESSAGE);
        messages.put("config_reloaded", DEFAULT_CONFIG_LOADED);
        return messages;
    }

    public static String getLevelBlockedMessage() {
        return formatMessage(getMessage("level_blocked", DEFAULT_LEVEL_BLOCKED_MESSAGE));
    }

    public static String getMaxLevelReachedMessage() {
        return formatMessage(getMessage("max_level_reached", DEFAULT_MAX_LEVEL_REACHED_MESSAGE));
    }

    public static String getCaptureBlockedMessage() {
        return formatMessage(getMessage("capture_blocked", DEFAULT_CAPTURE_BLOCKED_MESSAGE));
    }

    public static String getRightClickBlockedMessage() {
        return formatMessage(getMessage("interact_blocked", DEFAULT_INTERACT_BLOCKED_MESSAGE));
    }

    public static String getTradeBlockedMessage() {
        return formatMessage(getMessage("trade_blocked", DEFAULT_TRADE_BLOCKED_MESSAGE));
    }

    public static String getNPCTradeAccessMessage() {
        return formatMessage(getMessage("trade_access_message", DEFAULT_NPCTRADE_ACCESS_MESSAGE));
    }

    public static String getDefaultConfigLoaded() {
        return formatMessage(getMessage("config_reloaded", DEFAULT_CONFIG_LOADED));
    }

    private static String getMessage(String key, String defaultMessage) {
        Map<String, String> messages = (Map<String, String>) configData.get("messages");
        return messages != null ? messages.getOrDefault(key, defaultMessage) : defaultMessage;
    }

    private static String formatMessage(String message) {
        return translateAlternateColorCodes(message);
    }

    private static String translateAlternateColorCodes(String text) {
        return text.replace("&", "§");
    }

    public static int getBadgeLevel(int badgeCount) {
        String defaultLevel = DEFAULT_LEVELS[Math.min(badgeCount, DEFAULT_LEVELS.length - 1)];
        Map<String, Object> badgeLevels = (Map<String, Object>) configData.get("badge_levels");
        if (badgeLevels == null) {
            return Integer.parseInt(defaultLevel);
        }
        String levelStr = (String) badgeLevels.getOrDefault("badge.level." + badgeCount, defaultLevel);
        return Integer.parseInt(levelStr);
    }
}