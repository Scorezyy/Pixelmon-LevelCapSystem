package de.scorezy.pixelmonlevelcapsystem.listeners;

import com.pixelmonmod.pixelmon.items.BadgeCaseItem;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem.BadgeCase;
import com.pixelmonmod.pixelmon.items.BadgeItem;
import de.scorezy.pixelmonlevelcapsystem.configs.SettingsConfig;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pixelmonlevelcapsystem")
public class DuplicatedBadgeListener {

    @SubscribeEvent
    public static void onBadgeRightClick(RightClickItem event) {
        SettingsConfig settings = ConfigLoader.getSettingsConfig();
        if (!settings.isCheckDuplicateBadges()) {
            return;
        }

        PlayerEntity player = event.getPlayer();
        ItemStack held = event.getItemStack();

        if (!(held.getItem() instanceof BadgeItem)) {
            return;
        }

        String playerName = player.getName().getString();
        String newId = held.getItem().getRegistryName().toString();

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

        BadgeCase badgeCase = BadgeCase.readFromItemStack(caseStack);
        if (badgeCase == null || !badgeCase.isOwner(player)) {
            if (settings.isDebug()) {
                Logger.debug(playerName + " is not owner of BadgeCase");
            }
            return;
        }

        boolean isDuplicate = false;
        for (ItemStack existing : badgeCase.badges) {
            if (existing.getItem().getRegistryName().toString().equals(newId)) {
                isDuplicate = true;
                break;
            }
        }

        if (isDuplicate) {
            if (settings.isDebug()) {
                Logger.debug(playerName + " attempted duplicate badge: " + newId);
            }
            if (player instanceof ServerPlayerEntity) {
                ServerPlayerEntity sp = (ServerPlayerEntity) player;
                String message = ConfigLoader.getMessagesConfig().getDuplicateBadgesBlocked();
                sp.sendMessage(new StringTextComponent(message), sp.getUUID());
            }
            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.FAIL);
            return;
        }

        boolean added = BadgeCaseItem.addBadge(caseStack, player, held.copy());
        if (added) {
            held.shrink(1);
            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.SUCCESS);
            if (settings.isDebug()) {
                Logger.debug(playerName + " successfully added badge: " + newId);
            }
        } else {
            if (settings.isDebug()) {
                Logger.debug(playerName + " failed to add badge: " + newId);
            }
        }
    }
}
