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
                return getMaxLevel(badgeCount);
            }
        }
        return getDefaultLevel();
    }
    private static int getMaxLevel(int badgeCount) {
        return switch (badgeCount) {
            case 0 -> 10;
            case 1 -> 20;
            case 2 -> 30;
            case 3 -> 40;
            case 4 -> 50;
            case 5 -> 60;
            case 6 -> 70;
            case 7 -> 100;
            default -> 10; // Rückfallwert
        };
    }

    private static int getDefaultLevel() {
        return 10;
    }
}