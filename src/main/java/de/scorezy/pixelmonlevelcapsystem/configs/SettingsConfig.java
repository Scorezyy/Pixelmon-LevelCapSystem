package de.scorezy.pixelmonlevelcapsystem.configs;

import java.util.Map;

public class SettingsConfig {
    private boolean levelCapWildPokemons;
    private boolean levelCapLegendaryPokemons;
    private boolean blockCaptures;
    private boolean blockInteractions;
    private boolean blockLevelUps;
    private boolean blockExperienceGain;
    private boolean levelCapNpcTrades;
    private boolean levelCapRaidPokemons;
    private boolean levelCapPlayerTrades;
    private boolean checkDuplicateBadges;
    private boolean debug;

    public SettingsConfig() {
        setDefaults();
    }

    private void setDefaults() {
        this.levelCapWildPokemons = true;
        this.levelCapLegendaryPokemons = false;
        this.blockCaptures = true;
        this.blockInteractions = true;
        this.blockLevelUps = true;
        this.blockExperienceGain = true;
        this.levelCapNpcTrades = true;
        this.levelCapRaidPokemons = true;
        this.levelCapPlayerTrades = true;
        this.checkDuplicateBadges = true;
        this.debug = false;
    }

    @SuppressWarnings("unchecked")
    public void loadFromMap(Map<String, Object> data) {
        Object s = data.get("settings");
        if (s instanceof Map) {
            Map<String, Object> message = (Map<String, Object>) s;
            Object o;
            o = message.get("levelcap_wild_pokemons");
            if (o instanceof Boolean) this.levelCapWildPokemons = (Boolean) o;
            o = message.get("check_duplicate_badges");
            if (o instanceof Boolean) this.checkDuplicateBadges = (Boolean) o;
            o = message.get("levelcap_legendary_pokemons");
            if (o instanceof Boolean) this.levelCapLegendaryPokemons = (Boolean) o;
            o = message.get("levelcap_captures");
            if (o instanceof Boolean) this.blockCaptures = (Boolean) o;
            o = message.get("levelcap_interactions");
            if (o instanceof Boolean) this.blockInteractions = (Boolean) o;
            o = message.get("levelcap_level_ups");
            if (o instanceof Boolean) this.blockLevelUps = (Boolean) o;
            o = message.get("save_experience_gainsave_experience_gain");
            if (o instanceof Boolean) this.blockExperienceGain = (Boolean) o;
            o = message.get("levelcap_npc_trades");
            if (o instanceof Boolean) this.levelCapNpcTrades = (Boolean) o;
            o = message.get("levelcap_raid_pokemons");
            if (o instanceof Boolean) this.levelCapRaidPokemons = (Boolean) o;
            o = message.get("levelcap_player_trades");
            if (o instanceof Boolean) this.levelCapPlayerTrades = (Boolean) o;
        }
        Object d = data.get("debugging");
        if (d instanceof Map) {
            Object o = ((Map<String, Object>) d).get("debug");
            if (o instanceof Boolean) this.debug = (Boolean) o;
        }
    }

    public boolean isLevelCapWildPokemons() {
        return levelCapWildPokemons;
    }

    public boolean isLevelCapLegendaryPokemons() {
        return levelCapLegendaryPokemons;
    }

    public boolean isBlockCaptures() {
        return blockCaptures;
    }

    public boolean isBlockInteractions() {
        return blockInteractions;
    }

    public boolean isBlockLevelUps() { return blockLevelUps;}

    public boolean isBlockExperienceGain() {
        return blockExperienceGain;
    }

    public boolean isLevelCapNpcTrades() {
        return levelCapNpcTrades;
    }

    public boolean isLevelCapRaidPokemons() {
        return levelCapRaidPokemons;
    }

    public boolean isLevelCapPlayerTrades() {
        return levelCapPlayerTrades;
    }

    public boolean isCheckDuplicateBadges() {
        return checkDuplicateBadges;
    }

    public boolean isDebug() {
        return debug;
    }
}
