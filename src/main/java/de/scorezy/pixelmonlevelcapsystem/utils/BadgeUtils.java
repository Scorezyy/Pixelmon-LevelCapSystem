package de.scorezy.pixelmonlevelcapsystem.utils;

import com.pixelmonmod.pixelmon.init.registry.PixelmonDataComponents;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem.BadgeCase;
import de.scorezy.pixelmonlevelcapsystem.configs.BadgeLevelConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.TreeMap;

public class BadgeUtils {

    public static ItemStack findBadgeCaseItemStack(ServerPlayer player) {
        if (player == null) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = BadgeCaseItem.findFirstRegisteredBadgeCase(player);
        return stack != null ? stack : ItemStack.EMPTY;
    }

    public static int getMaxLevelForPlayer(ServerPlayer player) {
        BadgeLevelConfig blc = ConfigLoader.getBadgeLevelConfig();

        ItemStack stack = findBadgeCaseItemStack(player);
        if (!stack.isEmpty() && stack.getItem() instanceof BadgeCaseItem) {
            BadgeCase badgeCase = stack.get(PixelmonDataComponents.BADGE_CASE);
            if (badgeCase != null && badgeCase.isOwner(player)) {
                return getMaxLevelByBadgeCount(blc, badgeCase.badges().size());
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
