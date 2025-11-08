package de.scorezy.pixelmonlevelcapsystem.utils;

import de.scorezy.pixelmonlevelcapsystem.configs.BadgeLevelConfig;
import de.scorezy.pixelmonlevelcapsystem.configs.ExcludeLevelCapPokemonConfig;
import de.scorezy.pixelmonlevelcapsystem.configs.MessagesConfig;
import de.scorezy.pixelmonlevelcapsystem.configs.SettingsConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

import info.pixelmon.repack.org.spongepowered.yaml.internal.snakeyaml.Yaml;
import net.neoforged.fml.loading.FMLPaths;

public class ConfigLoader {
    private static BadgeLevelConfig badgeLevelConfig = new BadgeLevelConfig();
    private static MessagesConfig messagesConfig = new MessagesConfig();
    private static SettingsConfig settingsConfig = new SettingsConfig();
    private static ExcludeLevelCapPokemonConfig excludeLevelCapPokemonConfig = new ExcludeLevelCapPokemonConfig();

    private static Path configDir;
    private static Path badgeLevelPath;
    private static Path settingsPath;
    private static Path messagesPath;
    private static Path excludeLevelCapPath;
    private static Yaml yaml = new Yaml();

    static {
        configDir = FMLPaths.CONFIGDIR.get().resolve("levelcapsystem");
        badgeLevelPath = configDir.resolve("badgelevel.yml");
        settingsPath = configDir.resolve("settings.yml");
        messagesPath = configDir.resolve("messages.yml");
        excludeLevelCapPath = configDir.resolve("excludelevelcap.yml");
        try {
            Files.createDirectories(configDir);
        } catch (Exception ignored) { }
    }


    public static void loadConfig() {
        loadBadgeLevel();
        loadSettings();
        loadMessages();
        loadExcludeLevelCap();
    }

    private static void loadBadgeLevel() {
        String defaultContent =
                "#####################################################\n" +
                        "#    ___          __         __               __ \n" +
                        "#   / _ )___ ____/ ___ ____ / / ___ _  _____ / ___\n" +
                        "#  / _  / _ `/ _  / _ `/ -_/ /_/ -_| |/ / -_/ (_-<\n" +
                        "# /____/\\_,_/\\_,_/\\_, /\\__/____\\__/|___/\\__/_/___/\n" +
                        "#                /___/                          \n" +
                        "#       BadgeLevel System Configuration\n" +
                        "#####################################################\n" +
                        "\n" +
                        "# When true, the Mod ignores BadgeCase counts and uses permissions instead.\n" +
                        "use_permissions: false\n" +
                        "\n" +
                        "# ________________________________________________________________\n" +
                        "# Permission-based level caps: define as many ranks as you need.\n" +
                        "# After the \":\" specify the maximum Pokémon level allowed by that permission.\n" +
                        "# The mod checks every entry and applies the HIGHEST matching level cap.\n" +
                        "# You can add beyond badge.8, e.g. badge.9, badge.10, etc. — no hard limit!\n" +
                        "#\n" +
                        "# You can freely name the permissions, e.g.:\n" +
                        "#   rank.gold.levelcap, trainer.level.15, or my.custom.permission\n" +
                        "# Only the level value matters — not the permission name itself.\n" +
                        "# ________________________________________________________________\n" +
                        "permission_levels:\n" +
                        "  pixelmonlevelcap.badge.0:  10\n" +
                        "  pixelmonlevelcap.badge.1:  20\n" +
                        "  pixelmonlevelcap.badge.2:  30\n" +
                        "  pixelmonlevelcap.badge.3:  40\n" +
                        "  pixelmonlevelcap.badge.4:  50\n" +
                        "  pixelmonlevelcap.badge.5:  60\n" +
                        "  pixelmonlevelcap.badge.6:  70\n" +
                        "  pixelmonlevelcap.badge.7:  80\n" +
                        "  pixelmonlevelcap.badge.8: 100\n" +
                        "  # Add as many entries as you like — no hard limit!\n" +
                        "\n" +
                        "# ________________________________________________________________\n" +
                        "# Badge-count based level caps (used when use_permissions: false):\n" +
                        "# The mod reads how many badges are in the player's BadgeCase,\n" +
                        "# then looks up that count in this mapping.\n" +
                        "# If the player's badge count exceeds the highest defined key,\n" +
                        "# it will use the level for the highest defined badge-count entry.\n" +
                        "# You can list more than 8 entries—no hard limit!\n" +
                        "# ________________________________________________________________\n" +
                        "badge_levels:\n" +
                        "  0: 10\n" +
                        "  1: 20\n" +
                        "  2: 30\n" +
                        "  3: 40\n" +
                        "  4: 50\n" +
                        "  5: 60\n" +
                        "  6: 70\n" +
                        "  7: 80\n" +
                        "  8: 100\n" +
                        "  # Add as many entries as you like—no hard limit!\n";

        loadOrCreateYaml(
                badgeLevelPath.toFile(),
                defaultContent,
                badgeLevelConfig::loadFromMap,
                () -> badgeLevelConfig = new BadgeLevelConfig()
        );
    }

    private static void loadSettings() {
        String defaultContent =
                "#####################################################\n" +
                        "#    __               _______       \n" +
                        "#   / / ___ _  _____ / / ______ ____\n" +
                        "#  / /_/ -_| |/ / -_/ / /__/ _ `/ _ \\\n" +
                        "# /____\\__/|___/\\__/_/\\___/\\_,_/ .__/\n" +
                        "#                             /_/  \n" +
                        "#       LevelCap System Configuration Settings\n" +
                        "#####################################################\n" +
                        "\n" +
                        "settings:\n" +
                        "  # When enabled, wild Pokémon that spawn above your badge level cap\n" +
                        "  # will have their level reduced to match your current cap.\n" +
                        "  # The player with the fewest badges nearby determines the cap used.\n" +
                        "  # Maximum scan range for nearby players is 6 chunks.\n" +
                        "  # If disabled, Pokémon spawn at full level regardless of badge progress.\n" +
                        "  levelcap_wild_pokemons: true\n" +
                        "\n" +
                        "  # When enabled, players can only have one of each badge in their Badge Case.\n" +
                        "  # If disabled, duplicate badges are allowed.\n" +
                        "  check_duplicate_badges: true\n" +
                        "\n" +
                        "  # When enabled, legendary Pokémon can only be caught if their level is within your badge cap.\n" +
                        "  # If disabled, they can be caught regardless of level.\n" +
                        "  levelcap_legendary_pokemons: false\n" +
                        "\n" +
                        "  # When enabled, wild Pokémon can only be caught if their level is within your badge cap.\n" +
                        "  # If the Pokémon's level is too high, the capture will fail and your Pokéball will be returned.\n" +
                        "  # If disabled, you can catch any Pokémon regardless of level.\n" +
                        "  levelcap_captures: true\n" +
                        "\n" +
                        "  # When enabled, level-up items like Rare Candy or other EXP items work,\n" +
                        "  # but Pokémon stop gaining levels once they reach the level cap.\n" +
                        "  # If disabled, they can level up beyond the cap without restriction.\n" +
                        "  levelcap_interactions: true\n" +
                        "\n" +
                        "  # When enabled, Pokémon cannot level up from battle EXP if it would put them above the level cap.\n" +
                        "  # If disabled, they can gain levels from battle EXP beyond the cap without restriction.\n" +
                        "  levelcap_level_ups: true\n" +
                        "\n" +
                        "  # When enabled, any XP earned at the level cap is stored and applied once you earn a new badge.\n" +
                        "  # If disabled, Pokémon will always stay 1 XP short of leveling up and cannot gain more until the cap increases.\n" +
                        "  save_experience_gain: true\n" +
                        "\n" +
                        "  # When enabled, any Pokémon received from an NPC trade that is above your badge-level cap\n" +
                        "  # will have its level reduced to match your current cap.\n" +
                        "  # If disabled, the Pokémon keeps its original level, which may exceed your cap.\n" +
                        "  levelcap_npc_trades: true\n" +
                        "\n" +
                        "  # When enabled, raid-generated Pokémon that would exceed your badge-level cap\n" +
                        "  # will have their level reduced to your cap when caught — but only if their original level is higher.\n" +
                        "  # If disabled, they will keep their full level when caught, even if it exceeds your cap.\n" +
                        "  levelcap_raid_pokemons: true\n" +
                        "\n" +
                        "  # When enabled, player-to-player trades will check whether the recipient has enough badges\n" +
                        "  # to receive the Pokémon based on their level cap.\n" +
                        "  # If disabled, this check is skipped entirely, and trades are allowed regardless of badge count.\n" +
                        "  levelcap_player_trades: true\n" +
                        "\n" +
                        "# If true, log each cap or block event once to the server console for debugging purposes.\n" +
                        "debugging:\n" +
                        "  debug: true\n";

        loadOrCreateYaml(
                settingsPath.toFile(),
                defaultContent,
                settingsConfig::loadFromMap,
                () -> settingsConfig = new SettingsConfig()
        );
    }

    private static void loadMessages() {
        String defaultContent =
                "#####################################################\n" +
                        "#    __  ___                          \n" +
                        "#   /  |/  ___ ___ ______ ____ ____ ___\n" +
                        "#  / /|_/ / -_(_-<(_-/ _ `/ _ `/ -_(_-<\n" +
                        "# /_/  /_/\\__/___/___\\_,_/\\_, /\\__/___/\n" +
                        "#                        /___/       \n" +
                        "#       Messages System Configuration\n" +
                        "#####################################################\n" +
                        "\n" +
                        "messages:\n" +
                        "  level_blocked: \"&cThis level is too high for your Pokémon!\"\n" +
                        "  max_level_reached: \"&cYour &ePokémon &chas already reached the maximum level\"\n" +
                        "  capture_blocked: \"&cThis &ePokémon &cis too strong for you to catch!\"\n" +
                        "  interact_blocked: \"&cCan't give &bEXP &eitems&7; &cthis Pokémon is already at max level&7!\"\n" +
                        "  trade_blocked: \"&cYou cannot trade your Pokémon because it exceeds the allowed level!\"\n" +
                        "  trade_blocked_partner: \"&cThe other player does not have enough badges to trade this Pokémon with you!\"\n" +
                        "  npc_trade_access: \"&cThe level of the traded &ePokémon &chas been adjusted to match your &dbadge level&7!\"\n" +
                        "  overLevelMessage: \"&cYour Pokémon &e{pokemon} &chas reached the level cap! &aEXP stored&7: &e{exp}\"\n" +
                        "  duplicate_badges_blocked: \"&cYou cannot have duplicate badges in your BadgeCase!\"\n" +
                        "  config_reloaded: \"&aConfig has been successfully reloaded!\"\n";

        loadOrCreateYaml(
                messagesPath.toFile(),
                defaultContent,
                messagesConfig::loadFromMap,
                () -> messagesConfig = new MessagesConfig()
        );
    }


    private static void loadExcludeLevelCap() {
        String defaultContent =
                "#####################################################\n" +
                        "# Pokémon listed below are excluded from the level cap system\n" +
                        "# and will not be affected by its restrictions. \n" +
                        "#####################################################\n" +
                        "#\n " +
                        "excluded_pokemon:\n" +
                        "  - MissingNo\n";

        loadOrCreateYaml(
                excludeLevelCapPath.toFile(),
                defaultContent,
                excludeLevelCapPokemonConfig::loadFromMap,
                () -> excludeLevelCapPokemonConfig = new ExcludeLevelCapPokemonConfig()
        );
    }

    private static void loadOrCreateYaml(File file, String defaultContent,
                                         Consumer<Map<String, Object>> fromMapConsumer,
                                         Runnable onNewInstance) {
        boolean created = false;

        if (!file.exists()) {
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                writer.write(defaultContent);
                created = true;
            } catch (IOException ignored) { }
            onNewInstance.run();
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Object obj = yaml.load(reader);
            if (obj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) obj;
                fromMapConsumer.accept(data);
            }
        } catch (IOException ignored) { }
    }

    public static BadgeLevelConfig getBadgeLevelConfig() {
        return badgeLevelConfig;
    }

    public static MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public static SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }

    public static ExcludeLevelCapPokemonConfig getExcludeLevelCapPokemonConfig() {
        return excludeLevelCapPokemonConfig;
    }
}