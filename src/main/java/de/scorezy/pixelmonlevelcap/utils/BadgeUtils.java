package de.scorezy.pixelmonlevelcap.utils;

import com.pixelmonmod.pixelmon.items.BadgeCaseItem;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem.BadgeCase;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public class BadgeUtils {

    public static ItemStack findBadgeCaseItemStack(ServerPlayerEntity player) {
        return player.inventory.items.stream()
                .filter(stack -> stack.getItem() instanceof BadgeCaseItem)
                .findFirst()
                .orElse(null);
    }

    public static int getMaxLevelForPlayer(ServerPlayerEntity player) {
        ItemStack badgeCaseStack = findBadgeCaseItemStack(player);
        if (badgeCaseStack != null) {
            BadgeCase badgeCase = BadgeCaseItem.BadgeCase.readFromItemStack(badgeCaseStack);
            if (badgeCase != null && badgeCase.isOwner(player)) {
                int badgeCount = badgeCase.badges.size();
                int maxLevel = getMaxLevel(badgeCount);
                return maxLevel;
            }
        }
        return getDefaultLevel();
    }

    private static int getMaxLevel(int badgeCount) {
        return ConfigLoader.getBadgeLevel(badgeCount);
    }

    private static int getDefaultLevel() {
        return ConfigLoader.getBadgeLevel(0);
    }
}
