package de.scorezy.pixelmonlevelcapsystem.listeners;

import com.pixelmonmod.pixelmon.items.BadgeCaseItem;
import com.pixelmonmod.pixelmon.items.BadgeCaseItem.BadgeCase;
import com.pixelmonmod.pixelmon.items.BadgeItem;
import de.scorezy.pixelmonlevelcapsystem.configs.SettingsConfig;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = "pixelmonlevelcapsystem", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DuplicatedBadgeListener {

    @SubscribeEvent
    public static void onBadgeRightClick(RightClickItem event) {
        SettingsConfig settings = ConfigLoader.getSettingsConfig();
        if (!settings.isCheckDuplicateBadges()) return;

        Player player = event.getEntity();
        ItemStack held = event.getItemStack();
        if (!(held.getItem() instanceof BadgeItem)) return;

        String playerName = player.getName().getString();
        String newId = ForgeRegistries.ITEMS.getKey(held.getItem()).toString();
        if (settings.isDebug()) {
            Logger.debug(playerName + " attempting to add badge: " + newId);
        }

        ItemStack caseStack = BadgeCaseItem.findFirstRegisteredBadgeCase(player);
        if (caseStack == null || caseStack.isEmpty()) {
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

        for (ItemStack existing : badgeCase.badges) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(existing.getItem());
            if (id != null && id.toString().equals(newId)) {
                if (settings.isDebug()) {
                    Logger.debug(playerName + " attempted duplicate badge: " + newId);
                }
                if (player instanceof ServerPlayer sp) {
                    sp.sendSystemMessage(Component.literal(
                            ConfigLoader.getMessagesConfig().getDuplicateBadgesBlocked()
                    ));
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
                return;
            }
        }

        boolean added = BadgeCaseItem.addBadge(caseStack, player, held.copy());
        if (added) {
            held.shrink(1);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
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
