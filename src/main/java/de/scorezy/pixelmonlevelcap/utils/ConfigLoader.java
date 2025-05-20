package de.scorezy.pixelmonlevelcap.utils;


import info.pixelmon.repack.org.spongepowered.yaml.internal.snakeyaml.DumperOptions;
import info.pixelmon.repack.org.spongepowered.yaml.internal.snakeyaml.Yaml;
import net.neoforged.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ConfigLoader {

    private static final String CONFIG_FILE_NAME = "levelcap.yml";

    private static final String[] DEFAULT_LEVELS = {"10", "20", "30", "40", "50", "60", "70", "80", "100"};
    private static final boolean DEFAULT_CAPED_POKE_SPAWN = true;
    private static final boolean DEFAULT_DEBUG_MESSAGES = true;
    private static final boolean DEFAULT_LEGENDARY_LEVELCAP = false;

    private static final String DEFAULT_LEVEL_BLOCKED_MESSAGE = "&cThis level is too high for your Pokémon!";
    private static final String DEFAULT_MAX_LEVEL_REACHED_MESSAGE = "&cYour &ePokémon &chas already reached the maximum level";
    private static final String DEFAULT_CAPTURE_BLOCKED_MESSAGE = "&cThis &ePokémon &cis too strong for you to catch!";
    private static final String DEFAULT_INTERACT_BLOCKED_MESSAGE = "&cThe Pokémon is too high level for you!";
    private static final String DEFAULT_TRADE_BLOCKED_MESSAGE = "&cYou cannot trade your Pokémon because it exceeds the allowed level!";
    private static final String DEFAULT_TRADE_BLOCKED_PARTNER_MESSAGE = "&cThe other player does not have enough badges to trade this Pokémon with you!";
    private static final String DEFAULT_NPCTRADE_ACCESS_MESSAGE = "&aThe level of the traded Pokémon has been adjusted to match your badge level!";
    private static final String DEFAULT_CONFIG_LOADED = "&aConfig has been successfully reloaded!";

    private static Map<String, Object> configData = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public static void loadConfig() {
        Path configDir = FMLPaths.CONFIGDIR.get();
        File configFile = configDir.resolve(CONFIG_FILE_NAME).toFile();

        if (!configFile.exists()) {
            saveConfig(configFile, createDefaultConfigData());
        }

        Map<String, Object> yamlData;
        try (BufferedReader reader = Files.newBufferedReader(configFile.toPath(), StandardCharsets.UTF_8)) {
            Yaml yaml = new Yaml();
            Object loaded = yaml.load(reader);
            yamlData = loaded instanceof Map ? (Map<String, Object>) loaded : new LinkedHashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
            yamlData = new LinkedHashMap<>();
        }

        boolean changed = mergeDefaults(yamlData, createDefaultConfigData());

        if (changed) {
            saveConfig(configFile, yamlData);
        }

        configData = yamlData;
    }

    private static Map<String, Object> createDefaultConfigData() {
        Map<String, Object> defaults = new LinkedHashMap<>();

        Map<String, String> badgeLevels = new LinkedHashMap<>();
        IntStream.range(0, DEFAULT_LEVELS.length)
                .forEach(i -> badgeLevels.put("badge.level." + i, DEFAULT_LEVELS[i]));
        defaults.put("badge_levels", badgeLevels);

        Map<String, String> messages = new LinkedHashMap<>();
        messages.put("interact_blocked", DEFAULT_INTERACT_BLOCKED_MESSAGE);
        messages.put("capture_blocked", DEFAULT_CAPTURE_BLOCKED_MESSAGE);
        messages.put("level_blocked", DEFAULT_LEVEL_BLOCKED_MESSAGE);
        messages.put("max_level_reached", DEFAULT_MAX_LEVEL_REACHED_MESSAGE);
        messages.put("trade_blocked", DEFAULT_TRADE_BLOCKED_MESSAGE);
        messages.put("trade_blocked_partner", DEFAULT_TRADE_BLOCKED_PARTNER_MESSAGE);
        messages.put("npcTrade_access_message", DEFAULT_NPCTRADE_ACCESS_MESSAGE);
        messages.put("config_reloaded", DEFAULT_CONFIG_LOADED);
        defaults.put("messages", messages);

        defaults.put("caped_poke_spawn", DEFAULT_CAPED_POKE_SPAWN);
        defaults.put("debug_messages", DEFAULT_DEBUG_MESSAGES);
        defaults.put("legendary_levelcap", DEFAULT_LEGENDARY_LEVELCAP);

        return defaults;
    }

    @SuppressWarnings("unchecked")
    private static boolean mergeDefaults(Map<String, Object> data, Map<String, Object> defaults) {
        boolean changed = false;
        for (Map.Entry<String, Object> e : defaults.entrySet()) {
            String key = e.getKey();
            Object defVal = e.getValue();
            if (!data.containsKey(key)) {
                data.put(key, deepCopy(defVal));
                changed = true;
            } else if (defVal instanceof Map && data.get(key) instanceof Map) {
                boolean subChanged = mergeDefaults(
                        (Map<String, Object>) data.get(key),
                        (Map<String, Object>) defVal
                );
                if (subChanged) changed = true;
            }
        }
        return changed;
    }

    @SuppressWarnings("unchecked")
    private static Object deepCopy(Object obj) {
        if (obj instanceof Map) {
            Map<String, Object> src = (Map<String, Object>) obj;
            Map<String, Object> dst = new LinkedHashMap<>();
            for (Map.Entry<String, Object> e : src.entrySet()) {
                dst.put(e.getKey(), deepCopy(e.getValue()));
            }
            return dst;
        }
        return obj;
    }

    private static void saveConfig(File configFile, Map<String, Object> yamlData) {
        try {
            configFile.getParentFile().mkdirs();
            DumperOptions opts = new DumperOptions();
            opts.setIndent(2);
            opts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            opts.setPrettyFlow(true);
            Yaml yaml = new Yaml(opts);
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
                yaml.dump(yamlData, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static String getMessage(String key, String def) {
        Map<String, String> msgs = (Map<String, String>) configData.get("messages");
        return msgs != null ? msgs.getOrDefault(key, def) : def;
    }

    private static String fmt(String msg) {
        return msg.replace("&", "§");
    }

    public static String getLevelBlockedMessage() {
        return fmt(getMessage("level_blocked", DEFAULT_LEVEL_BLOCKED_MESSAGE));
    }

    public static String getMaxLevelReachedMessage() {
        return fmt(getMessage("max_level_reached", DEFAULT_MAX_LEVEL_REACHED_MESSAGE));
    }

    public static String getCaptureBlockedMessage() {
        return fmt(getMessage("capture_blocked", DEFAULT_CAPTURE_BLOCKED_MESSAGE));
    }

    public static String getRightClickBlockedMessage() {
        return fmt(getMessage("interact_blocked", DEFAULT_INTERACT_BLOCKED_MESSAGE));
    }

    public static String getTradeBlockedMessage() {
        return fmt(getMessage("trade_blocked", DEFAULT_TRADE_BLOCKED_MESSAGE));
    }

    public static String getTradeBlockedPartnerMessage() {
        return fmt(getMessage("trade_blocked_partner", DEFAULT_TRADE_BLOCKED_PARTNER_MESSAGE));
    }

    public static String getNPCTradeAccessMessage() {
        return fmt(getMessage("npcTrade_access_message", DEFAULT_NPCTRADE_ACCESS_MESSAGE));
    }

    public static String getDefaultConfigLoaded() {
        return fmt(getMessage("config_reloaded", DEFAULT_CONFIG_LOADED));
    }

    @SuppressWarnings("unchecked")
    public static int getBadgeLevel(int badgeCount) {
        Map<String, String> bl = (Map<String, String>) configData.get("badge_levels");
        String def = DEFAULT_LEVELS[Math.min(badgeCount, DEFAULT_LEVELS.length - 1)];
        String lvl = bl != null ? bl.getOrDefault("badge.level." + badgeCount, def) : def;
        return Integer.parseInt(lvl);
    }

    public static boolean isCapedPokeSpawnEnabled() {
        return (Boolean) configData.getOrDefault("caped_poke_spawn", DEFAULT_CAPED_POKE_SPAWN);
    }

    public static boolean isDebugMessagesEnabled() {
        return (Boolean) configData.getOrDefault("debug_messages", DEFAULT_DEBUG_MESSAGES);
    }

    public static boolean isLegendaryLevelCapEnabled() {
        return (Boolean) configData.getOrDefault("legendary_levelcap", DEFAULT_LEGENDARY_LEVELCAP);
    }
}
