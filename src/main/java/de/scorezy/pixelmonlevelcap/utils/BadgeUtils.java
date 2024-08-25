package de.scorezy.pixelmonlevelcap.utils;

import com.pixelmonmod.pixelmon.items.BadgeCaseItem;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem.BadgeCase;
import de.scorezy.pixelmonlevelcap.PixelmonLevelCap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BadgeUtils {

    /**
     * Get the level cap for the specified player based on his/her own badge count
     *
     * @param player specified player
     * @return the player's current level cap
     */
    public static int getLevelCapForPlayer(ServerPlayerEntity player) {
        int ownedBadgeCount = countOwnedBadges(player);
        return getBadgeLevel(ownedBadgeCount);
    }

    /**
     * Check and add up the number of badges in all badge cases belonging to the specified player.<br>
     * The checking is done in both player hand slots and player inventory
     *
     * @param player specified player
     * @return the total number of badges in badge cases belonging to this player
     */
    public static int countOwnedBadges(ServerPlayerEntity player) {
        return Optional.ofNullable(player)
                // Flatten hand slots & inventory into one stream for easier traversal
                .map(p -> Stream.concat(p.inventory.items.stream(),
                        StreamSupport.stream(p.getHandSlots().spliterator(), false)))
                .map(stream -> stream.filter(stack -> stack != null && stack.getItem() instanceof BadgeCaseItem)
                        .map(BadgeCase::readFromItemStack)
                        .filter(badgeCase -> badgeCase.isOwner(player))
                        .mapToInt(badgeCase -> badgeCase.badges.size())
                        .sum())
                .orElse(0);
    }

    /**
     * Calculate the pokemon level cap corresponding to the badge count
     *
     * @param badgeCount the count of badges
     * @return corresponding pokemon level cap
     */
    private static int getBadgeLevel(int badgeCount) {
        List<Integer> cappedLevels = PixelmonLevelCap.getInstance().getConfig().getCappedLevels();
        return !cappedLevels.isEmpty() ?
                cappedLevels.get(MathHelper.clamp(badgeCount, 0, cappedLevels.size() - 1)) : 100;
    }
}