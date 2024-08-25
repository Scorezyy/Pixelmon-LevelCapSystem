package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CaptureEventListener {

    @SubscribeEvent
    public void blockViaCaptureAttempt(CaptureEvent.StartCapture event) {
        ServerPlayerEntity player = event.getPlayer();
        int pokemonLevel = event.getPokemon().getLvl().getPokemonLevel();
        int maxLevel = BadgeUtils.getLevelCapForPlayer(player);

        if (isCaptureBlocked(pokemonLevel, maxLevel)) {
            handleCaptureRestriction(event, player);
        }
    }

    private boolean isCaptureBlocked(int pokemonLevel, int maxLevel) {
        return pokemonLevel > maxLevel;
    }

    private void handleCaptureRestriction(CaptureEvent.StartCapture event, ServerPlayerEntity player) {
        cancelEvent(event, player);
        returnBallToPlayer(event, player);
    }

    private void cancelEvent(CaptureEvent.StartCapture event, ServerPlayerEntity player) {
        event.setCanceled(true);
        player.sendMessage(new TranslationTextComponent("pixelmonlevelcap.capped_msg.capture_blocked"),
                player.getUUID());
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
