package de.scorezy.pixelmonlevelcapsystem.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import de.scorezy.pixelmonlevelcapsystem.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CaptureEventListener {

    @SubscribeEvent
    public void blockViaCaptureAttempt(CaptureEvent.StartCapture event) {
        if (!ConfigLoader.getSettingsConfig().isBlockCaptures()) {
            return;
        }

        ServerPlayerEntity player = event.getPlayer();
        String playerName = player.getName().getString();
        String pokemonName = event.getPokemon().getPokemonName();
        int pokemonLevel = event.getPokemon().getLvl().getPokemonLevel();
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

    private void handleCaptureRestriction(CaptureEvent.StartCapture event, ServerPlayerEntity player) {
        cancelEvent(event, player);
        returnBallToPlayer(event, player);
    }

    private void cancelEvent(CaptureEvent.StartCapture event, ServerPlayerEntity player) {
        event.setCanceled(true);
        String message = ConfigLoader.getMessagesConfig().getCaptureBlocked();
        player.sendMessage(new StringTextComponent(message), player.getUUID());
    }

    private void returnBallToPlayer(CaptureEvent.StartCapture event, ServerPlayerEntity player) {
        ItemStack ballStack = event.getPokeBall().getBallType().getBallItem();
        if (!ballStack.isEmpty()) {
            addBallToInventory(player, ballStack);
        }
    }

    private void addBallToInventory(ServerPlayerEntity player, ItemStack ballStack) {
        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
            ItemStack slotStack = player.inventory.getItem(i);
            if (slotStack.isEmpty()) {
                player.inventory.setItem(i, ballStack.copy());
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
