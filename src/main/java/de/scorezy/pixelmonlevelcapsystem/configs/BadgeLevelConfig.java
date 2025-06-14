package de.scorezy.pixelmonlevelcapsystem.configs;

import java.util.HashMap;
import java.util.Map;

public class BadgeLevelConfig {
    private boolean usePermissions;
    private Map<String, Integer> permissionLevels;
    private Map<Integer, Integer> badgeLevels;

    public BadgeLevelConfig() {
        setDefaults();
    }

    private void setDefaults() {
        this.usePermissions = false;
        this.permissionLevels = new HashMap<>();
        this.badgeLevels = new HashMap<>();
        this.permissionLevels.put("pixelmonlevelcap.badge.0", 10);
        this.permissionLevels.put("pixelmonlevelcap.badge.1", 20);
        this.permissionLevels.put("pixelmonlevelcap.badge.2", 30);
        this.permissionLevels.put("pixelmonlevelcap.badge.3", 40);
        this.permissionLevels.put("pixelmonlevelcap.badge.4", 50);
        this.permissionLevels.put("pixelmonlevelcap.badge.5", 60);
        this.permissionLevels.put("pixelmonlevelcap.badge.6", 70);
        this.permissionLevels.put("pixelmonlevelcap.badge.7", 80);
        this.permissionLevels.put("pixelmonlevelcap.badge.8", 100);
        this.badgeLevels.put(0, 10);
        this.badgeLevels.put(1, 20);
        this.badgeLevels.put(2, 30);
        this.badgeLevels.put(3, 40);
        this.badgeLevels.put(4, 50);
        this.badgeLevels.put(5, 60);
        this.badgeLevels.put(6, 70);
        this.badgeLevels.put(7, 80);
        this.badgeLevels.put(8, 100);
    }

    @SuppressWarnings("unchecked")
    public void loadFromMap(Map<String, Object> data) {
        Object up = data.get("use_permissions");
        if (up instanceof Boolean) {
            this.usePermissions = (Boolean) up;
        }
        Object pl = data.get("permission_levels");
        this.permissionLevels.clear();
        if (pl instanceof Map) {
            Map<String, Object> pm = (Map<String, Object>) pl;
            for (Map.Entry<String, Object> e : pm.entrySet()) {
                Object v = e.getValue();
                if (v instanceof Number) {
                    this.permissionLevels.put(e.getKey(), ((Number)v).intValue());
                }
            }
        }
        Object bl = data.get("badge_levels");
        this.badgeLevels.clear();
        if (bl instanceof Map) {
            Map<Object, Object> bm = (Map<Object, Object>) bl;
            for (Map.Entry<Object, Object> e : bm.entrySet()) {
                Object key = e.getKey();
                Object v = e.getValue();
                try {
                    Integer ik = Integer.parseInt(key.toString());
                    if (v instanceof Number) {
                        this.badgeLevels.put(ik, ((Number)v).intValue());
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }


    public boolean isUsePermissions() {
        return usePermissions;
    }

    public Map<String, Integer> getPermissionLevels() {
        return new HashMap<>(permissionLevels);
    }

    public Map<Integer, Integer> getBadgeLevels() {
        return new HashMap<>(badgeLevels);
    }
}
