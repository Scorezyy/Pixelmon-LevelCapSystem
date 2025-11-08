package de.scorezy.pixelmonlevelcapsystem.listeners;
import com.pixelmonmod.pixelmon.init.registry.PixelmonDataComponents;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem.BadgeCase;
import com.pixelmonmod.pixelmon.items.BadgeItem;
import de.scorezy.pixelmonlevelcapsystem.configs.SettingsConfig;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
public class DuplicatedBadgeListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBadgeRightClick(PlayerInteractEvent.RightClickItem event) {
        SettingsConfig settings = ConfigLoader.getSettingsConfig();
        if (!settings.isCheckDuplicateBadges()) {
            return;
        }

        Player player = event.getEntity();
        ItemStack held = event.getItemStack();

        if (!(held.getItem() instanceof BadgeItem)) {
            return;
        }

        String playerName = player.getName().getString();
        String newId = held.getItem().toString();

        if (settings.isDebug()) {
            Logger.debug(playerName + " attempting to add badge: " + newId);
        }

        ItemStack caseStack = BadgeCaseItem.findFirstRegisteredBadgeCase(player);
        if (caseStack == null) {
            if (settings.isDebug()) {
                Logger.debug(playerName + " has no BadgeCase");
            }
            return;
        }

        BadgeCase badgeCase = caseStack.get(PixelmonDataComponents.BADGE_CASE);
        if (badgeCase == null || !badgeCase.isOwner(player)) {
            if (settings.isDebug()) {
                Logger.debug(playerName + " is not owner of BadgeCase");
            }
            return;
        }

        boolean isDuplicate = false;
        for (ItemStack existing : badgeCase.badges()) {
            if (existing.getItem().toString().equals(newId)) {
                isDuplicate = true;
                break;
            }
        }

        if (isDuplicate) {
            if (settings.isDebug()) {
                Logger.debug(playerName + " attempted duplicate badge: " + newId);
            }
            if (player instanceof ServerPlayer sp) {
                String message = ConfigLoader.getMessagesConfig().getDuplicateBadgesBlocked();
                sp.sendSystemMessage(Component.literal(message));
            }
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        } else {
            if (settings.isDebug()) {
                Logger.debug(playerName + " allowed to add badge: " + newId);
            }
        }
    }
}
