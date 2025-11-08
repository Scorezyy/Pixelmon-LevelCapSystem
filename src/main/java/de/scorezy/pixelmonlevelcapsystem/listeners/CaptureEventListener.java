package de.scorezy.pixelmonlevelcapsystem.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import de.scorezy.pixelmonlevelcapsystem.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;

public class CaptureEventListener {

    @SubscribeEvent
    public void blockViaCaptureAttempt(CaptureEvent.StartCapture event) {
        if (!ConfigLoader.getSettingsConfig().isBlockCaptures()) {
            return;
        }

        ServerPlayer player = event.getPlayer();
        String playerName = player.getName().getString();
        String pokemonName = event.getPokemon().getSpecies().getName();
        int pokemonLevel = event.getPokemon().getPokemonLevel();

        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

        if (pokemonLevel > maxLevel) {
            Logger.debug(playerName + " tried to catch " + pokemonName +
                    " (lvl " + pokemonLevel + ") but cap is " + maxLevel);
            handleCaptureRestriction(event, player);
        } else {
            Logger.debug(playerName + " is allowed to catch " + pokemonName +
                    " (lvl " + pokemonLevel + "), cap is " + maxLevel);
        }
    }

    private void handleCaptureRestriction(CaptureEvent.StartCapture event, ServerPlayer player) {
        cancelEvent(event, player);
        returnBallToPlayer(event, player);
    }

    private void cancelEvent(CaptureEvent.StartCapture event, ServerPlayer player) {
        event.setCanceled(true);
        String message = ConfigLoader.getMessagesConfig().getCaptureBlocked();
        player.sendSystemMessage(Component.literal(message));
    }

    private void returnBallToPlayer(CaptureEvent.StartCapture event, ServerPlayer player) {
        ItemStack ballStack = event.getPokeBall().getBallItem();
        if (!ballStack.isEmpty()) {
            addBallToInventory(player, ballStack);
        }
    }

    private void addBallToInventory(ServerPlayer player, ItemStack ballStack) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slotStack = player.getInventory().getItem(i);
            if (slotStack.isEmpty()) {
                player.getInventory().setItem(i, ballStack.copy());
                return;
            }
            if (slotStack.getItem() == ballStack.getItem()) {
                addToStack(slotStack, ballStack);
                if (ballStack.isEmpty()) return;
            }
        }
    }

    private void addToStack(ItemStack slotStack, ItemStack ballStack) {
        int space = slotStack.getMaxStackSize() - slotStack.getCount();
        int amountToAdd = Math.min(space, ballStack.getCount());
        slotStack.grow(amountToAdd);
        ballStack.shrink(amountToAdd);
    }
}
