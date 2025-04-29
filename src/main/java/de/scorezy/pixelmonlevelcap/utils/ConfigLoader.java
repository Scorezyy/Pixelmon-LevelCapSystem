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
import java.util.stream.IntStream;

public class ConfigLoader {

    private static final String CONFIG_FILE_NAME = "levelcap.yml";
    private static final String[] DEFAULT_LEVELS = {"10","20","30","40","50","60","70","80","100"};
    private static final boolean DEFAULT_CAPED_POKE_SPAWN = true;
    private static final boolean DEFAULT_DEBUG_MESSAGES   = true;

    private static final String DEFAULT_LEVEL_BLOCKED_MESSAGE      = "&cThis level is too high for your Pokémon!";
    private static final String DEFAULT_MAX_LEVEL_REACHED_MESSAGE  = "&cYour &ePokémon &chas already reached the maximum level";
    private static final String DEFAULT_CAPTURE_BLOCKED_MESSAGE    = "&cThis &ePokémon &cis too strong for you to catch!";
    private static final String DEFAULT_INTERACT_BLOCKED_MESSAGE   = "&cThe Pokémon is too high level for you!";
    private static final String DEFAULT_TRADE_BLOCKED_MESSAGE      = "&cYou cannot trade your Pokémon because it exceeds the allowed level!";
    private static final String DEFAULT_NPCTRADE_ACCESS_MESSAGE    = "&aThe level of the traded Pokémon has been adjusted to match your badge level!";
    private static final String DEFAULT_CONFIG_LOADED              = "&aConfig has been successfully reloaded!";

    private static Map<String, Object> configData = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void loadConfig() {
        Path configDir = FMLPaths.CONFIGDIR.get();
        File configFile = new File(configDir.toFile(), CONFIG_FILE_NAME);

        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        }

        Map<String,Object> yamlData;
        try (BufferedReader reader = Files.newBufferedReader(configFile.toPath(), StandardCharsets.UTF_8)) {
            Yaml yaml = new Yaml();
            Object loaded = yaml.load(reader);
            yamlData = loaded instanceof Map ? (Map<String,Object>) loaded : new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
            yamlData = new HashMap<>();
        }

        boolean changed = false;

        if (!yamlData.containsKey("badge_levels")) {
            yamlData.put("badge_levels", createBadgeLevels());
            changed = true;
        } else {
            Map<String,String> badges = (Map<String,String>) yamlData.get("badge_levels");
            for (int i = 0; i < DEFAULT_LEVELS.length; i++) {
                String key = "badge.level." + i;
                if (!badges.containsKey(key)) {
                    badges.put(key, DEFAULT_LEVELS[i]);
                    changed = true;
                }
            }
        }

        if (!yamlData.containsKey("messages")) {
            yamlData.put("messages", createMessages());
            changed = true;
        } else {
            Map<String,String> msgs = (Map<String,String>) yamlData.get("messages");
            for (Map.Entry<String,String> e : createMessages().entrySet()) {
                if (!msgs.containsKey(e.getKey())) {
                    msgs.put(e.getKey(), e.getValue());
                    changed = true;
                }
            }
        }

        if (!yamlData.containsKey("caped_poke_spawn")) {
            yamlData.put("caped_poke_spawn", DEFAULT_CAPED_POKE_SPAWN);
            changed = true;
        }
        if (!yamlData.containsKey("debug_messages")) {
            yamlData.put("debug_messages", DEFAULT_DEBUG_MESSAGES);
            changed = true;
        }

        if (changed) {
            saveConfig(configFile, yamlData);
        }

        configData = yamlData;
    }

    private static void saveConfig(File configFile, Map<String,Object> yamlData) {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        try {
            configFile.getParentFile().mkdirs();
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
                yaml.dump(yamlData, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDefaultConfig(File configFile) {
        Map<String,Object> yamlData = new HashMap<>();
        yamlData.put("badge_levels", createBadgeLevels());
        yamlData.put("messages", createMessages());
        yamlData.put("caped_poke_spawn", DEFAULT_CAPED_POKE_SPAWN);
        yamlData.put("debug_messages", DEFAULT_DEBUG_MESSAGES);
        saveConfig(configFile, yamlData);
    }

    private static Map<String, String> createBadgeLevels() {
        Map<String, String> badgeLevels = new HashMap<>();
        IntStream.range(0, DEFAULT_LEVELS.length)
                .forEach(i -> badgeLevels.put("badge.level." + i, DEFAULT_LEVELS[i]));
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

    public static String getLevelBlockedMessage()      { return formatMessage(getMessage("level_blocked", DEFAULT_LEVEL_BLOCKED_MESSAGE)); }
    public static String getMaxLevelReachedMessage()  { return formatMessage(getMessage("max_level_reached", DEFAULT_MAX_LEVEL_REACHED_MESSAGE)); }
    public static String getCaptureBlockedMessage()   { return formatMessage(getMessage("capture_blocked", DEFAULT_CAPTURE_BLOCKED_MESSAGE)); }
    public static String getRightClickBlockedMessage(){ return formatMessage(getMessage("interact_blocked", DEFAULT_INTERACT_BLOCKED_MESSAGE)); }
    public static String getTradeBlockedMessage()     { return formatMessage(getMessage("trade_blocked", DEFAULT_TRADE_BLOCKED_MESSAGE)); }
    public static String getNPCTradeAccessMessage()   { return formatMessage(getMessage("npcTrade_access_message", DEFAULT_NPCTRADE_ACCESS_MESSAGE)); }
    public static String getDefaultConfigLoaded()     { return formatMessage(getMessage("config_reloaded", DEFAULT_CONFIG_LOADED)); }

    @SuppressWarnings("unchecked")
    private static String getMessage(String key, String def) {
        Map<String, String> msgs = (Map<String,String>) configData.get("messages");
        return msgs != null ? msgs.getOrDefault(key, def) : def;
    }

    private static String formatMessage(String msg) {
        return msg.replace("&", "§");
    }

    @SuppressWarnings("unchecked")
    public static int getBadgeLevel(int badgeCount) {
        String defLevel = DEFAULT_LEVELS[Math.min(badgeCount, DEFAULT_LEVELS.length - 1)];
        Map<String,Object> bl = (Map<String,Object>) configData.get("badge_levels");
        String lvl = bl != null ? (String) bl.getOrDefault("badge.level." + badgeCount, defLevel) : defLevel;
        return Integer.parseInt(lvl);
    }

    public static boolean isCapedPokeSpawnEnabled() {
        Object val = configData.get("caped_poke_spawn");
        return val instanceof Boolean ? (Boolean) val : DEFAULT_CAPED_POKE_SPAWN;
    }

    public static boolean isDebugMessagesEnabled() {
        Object val = configData.get("debug_messages");
        return val instanceof Boolean ? (Boolean) val : DEFAULT_DEBUG_MESSAGES;
    }
}
