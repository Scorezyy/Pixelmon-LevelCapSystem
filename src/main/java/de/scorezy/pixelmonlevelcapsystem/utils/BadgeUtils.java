package de.scorezy.pixelmonlevelcapsystem.utils;

import com.pixelmonmod.pixelmon.items.BadgeCaseItem;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem.BadgeCase;
import de.scorezy.pixelmonlevelcapsystem.configs.BadgeLevelConfig;
import de.scorezy.pixelmonlevelcapsystem.configs.SettingsConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.server.permission.PermissionAPI;

import java.util.Map;
import java.util.TreeMap;

public class BadgeUtils {

    public static ItemStack findBadgeCaseItemStack(ServerPlayerEntity player) {
        if (player == null || player.inventory == null) {
            return null;
        }
        return player.inventory.items.stream()
                .filter(stack -> stack != null && stack.getItem() instanceof BadgeCaseItem)
                .findFirst()
                .orElse(null);
    }

    public static int getMaxLevelForPlayer(ServerPlayerEntity player) {
        BadgeLevelConfig blc = ConfigLoader.getBadgeLevelConfig();
        SettingsConfig sc = ConfigLoader.getSettingsConfig();

        if (blc.isUsePermissions()) {
            return blc.getPermissionLevels().entrySet().stream()
                    .filter(e -> PermissionAPI.hasPermission(player, e.getKey()))
                    .mapToInt(Map.Entry::getValue)
                    .max()
                    .orElse(getDefaultBadgeLevel(blc));
        }

        ItemStack stack = findBadgeCaseItemStack(player);
        if (stack != null) {
            BadgeCase badgeCase = BadgeCaseItem.BadgeCase.readFromItemStack(stack);
            if (badgeCase != null && badgeCase.isOwner(player)) {
                return getMaxLevelByBadgeCount(blc, badgeCase.badges.size());
            }
        }

        return getDefaultBadgeLevel(blc);
    }

    private static int getDefaultBadgeLevel(BadgeLevelConfig blc) {
        Map<Integer, Integer> badgeLevels = blc.getBadgeLevels();
        if (badgeLevels.containsKey(0)) {
            return badgeLevels.get(0);
        }
        return badgeLevels.values().stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);
    }

    private static int getMaxLevelByBadgeCount(BadgeLevelConfig blc, int badgeCount) {
        TreeMap<Integer, Integer> sorted = new TreeMap<>(blc.getBadgeLevels());
        if (sorted.containsKey(badgeCount)) {
            return sorted.get(badgeCount);
        }
        Integer floorKey = sorted.floorKey(badgeCount);
        if (floorKey != null) {
            return sorted.get(floorKey);
        }
        return sorted.firstEntry().getValue();
    }
}
