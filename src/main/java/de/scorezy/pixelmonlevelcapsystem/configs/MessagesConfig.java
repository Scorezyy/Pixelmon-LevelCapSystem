package de.scorezy.pixelmonlevelcapsystem.configs;

import java.util.Map;

public class MessagesConfig {
    private String levelBlocked;
    private String maxLevelReached;
    private String captureBlocked;
    private String interactBlocked;
    private String tradeBlocked;
    private String tradeBlockedPartner;
    private String npcTradeAccess;
    private String configReloaded;
    private String duplicateBadgesBlocked;
    private String overLevelMessage;

    public MessagesConfig() {
        setDefaults();
    }

    private void setDefaults() {
        this.levelBlocked = "&cThis level is too high for your Pokémon!";
        this.maxLevelReached = "&cYour &ePokémon &chas already reached the maximum level";
        this.captureBlocked = "&cThis &ePokémon &cis too strong for you to catch!";
        this.interactBlocked = "&cCan't give &bEXP &eitems&7; &cthis Pokémon is already at max level&7!";
        this.tradeBlocked = "&cYou cannot trade your Pokémon because it exceeds the allowed level!";
        this.tradeBlockedPartner = "&cThe other player does not have enough badges to trade this Pokémon with you!";
        this.npcTradeAccess = "&aThe level of the traded Pokémon has been adjusted to match your badge level!";
        this.overLevelMessage = "&cYour Pokémon &e{pokemon} &chas reached the level cap! &aEXP stored&7: &e{exp}";
        this.configReloaded = "&aConfig has been successfully reloaded!";
        this.duplicateBadgesBlocked = "&cYou cannot have duplicate badges in your BadgeCase!";
    }

    @SuppressWarnings("unchecked")
    public void loadFromMap(Map<String, Object> data) {
        Object mb = data.get("messages");
        if (mb instanceof Map) {
            Map<String, Object> m = (Map<String, Object>) mb;
            this.levelBlocked          = parse((String) m.getOrDefault("level_blocked", this.levelBlocked));
            this.maxLevelReached       = parse((String) m.getOrDefault("max_level_reached", this.maxLevelReached));
            this.captureBlocked        = parse((String) m.getOrDefault("capture_blocked", this.captureBlocked));
            this.interactBlocked       = parse((String) m.getOrDefault("interact_blocked", this.interactBlocked));
            this.tradeBlocked          = parse((String) m.getOrDefault("trade_blocked", this.tradeBlocked));
            this.tradeBlockedPartner   = parse((String) m.getOrDefault("trade_blocked_partner", this.tradeBlockedPartner));
            this.npcTradeAccess        = parse((String) m.getOrDefault("npc_trade_access", this.npcTradeAccess));
            this.configReloaded        = parse((String) m.getOrDefault("config_reloaded", this.configReloaded));
            this.duplicateBadgesBlocked = parse((String) m.getOrDefault("duplicate_badges_blocked", this.duplicateBadgesBlocked));
            this.overLevelMessage      = parse((String) m.getOrDefault("overLevelMessage", this.overLevelMessage));
        }
    }

    private String parse(String input) {
        return input.replace("&", "§");
    }

    public String getLevelBlocked() {
        return levelBlocked;
    }

    public String getMaxLevelReached() {
        return maxLevelReached;
    }

    public String getCaptureBlocked() {
        return captureBlocked;
    }

    public String getInteractBlocked() {
        return interactBlocked;
    }

    public String getTradeBlocked() {
        return tradeBlocked;
    }

    public String getTradeBlockedPartner() {
        return tradeBlockedPartner;
    }

    public String getNpcTradeAccess() {
        return npcTradeAccess;
    }

    public String getConfigReloaded() {
        return configReloaded;
    }

    public String getDuplicateBadgesBlocked() {
        return duplicateBadgesBlocked;
    }

    public String getOverLevelMessage() {
        return overLevelMessage;
    }
}
