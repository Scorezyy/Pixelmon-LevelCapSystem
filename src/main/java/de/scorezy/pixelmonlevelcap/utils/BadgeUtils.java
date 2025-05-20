package de.scorezy.pixelmonlevelcap.utils;

import com.pixelmonmod.pixelmon.items.BadgeCaseItem;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem.BadgeCase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class BadgeUtils {

    public static ItemStack findBadgeCaseItemStack(ServerPlayer player) {
        if (player == null) {
            return null;
        }

        return player.getInventory().items.stream()
                .filter(stack -> stack != null && stack.getItem() instanceof BadgeCaseItem)
                .findFirst()
                .orElse(null);
    }

    public static int getMaxLevelForPlayer(ServerPlayer player) {
        ItemStack badgeCaseStack = findBadgeCaseItemStack(player);
        if (badgeCaseStack == null) {
            return getDefaultLevel();
        }

        BadgeCase badgeCase = BadgeCaseItem.BadgeCase.readFromItemStack(badgeCaseStack);
        if (badgeCase != null && badgeCase.isOwner(player)) {
            int badgeCount = badgeCase.badges().size();
            return getMaxLevel(badgeCount);
        } else {
            return getDefaultLevel();
        }
    }

    private static int getMaxLevel(int badgeCount) {
        return ConfigLoader.getBadgeLevel(badgeCount);
    }

    private static int getDefaultLevel() {
        return ConfigLoader.getBadgeLevel(0);
    }
}