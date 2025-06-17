package de.scorezy.pixelmonlevelcapsystem.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import de.scorezy.pixelmonlevelcapsystem.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CaptureEventListener {

    @SubscribeEvent
    public void blockViaCaptureAttempt(CaptureEvent.StartCapture event) {
        if (!ConfigLoader.getSettingsConfig().isBlockCaptures()) return;

        ServerPlayer player = (ServerPlayer) event.getPlayer();
        String playerName   = player.getName().getString();
        String pokemonName  = event.getPokemon().getPokemonName();
        int pokemonLevel    = event.getPokemon().getLvl().getPokemonLevel();
        int maxLevel        = BadgeUtils.getMaxLevelForPlayer(player);

        if (pokemonLevel <= maxLevel) {
            Logger.debug(playerName + " is allowed to catch " + pokemonName +
                    " (lvl " + pokemonLevel + "), cap is " + maxLevel);
            return;
        }

        Logger.debug(playerName + " tried to catch " + pokemonName +
                " (lvl " + pokemonLevel + ") but cap is " + maxLevel);
        event.setCanceled(true);
        player.sendSystemMessage(Component.literal(
                ConfigLoader.getMessagesConfig().getCaptureBlocked()
        ));

        ItemStack ballStack = event.getPokeBall().getBallType().getBallItem();
        if (!ballStack.isEmpty()) {
            addBallToInventory(player, ballStack);
        }
    }

    private void addBallToInventory(ServerPlayer player, ItemStack ballStack) {
        Inventory inv = player.getInventory();
        for (int i = 0, sz = inv.getContainerSize(); i < sz; i++) {
            ItemStack slot = inv.getItem(i);
            if (slot.isEmpty()) {
                inv.setItem(i, ballStack.copy());
                return;
            }
            if (slot.getItem() == ballStack.getItem()) {
                int space = slot.getMaxStackSize() - slot.getCount();
                int toAdd = Math.min(space, ballStack.getCount());
                slot.grow(toAdd);
                ballStack.shrink(toAdd);
                if (ballStack.isEmpty()) return;
            }
        }
    }
}
