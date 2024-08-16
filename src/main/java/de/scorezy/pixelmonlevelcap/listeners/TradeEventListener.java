package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TradeEventListener {

    @SubscribeEvent
    public void onPixelmonTrade(PixelmonTradeEvent.Pre event) {
        ServerPlayerEntity player1 = (ServerPlayerEntity) event.getPlayer1();
        ServerPlayerEntity player2 = (ServerPlayerEntity) event.getPlayer2();
        boolean shouldCancel = false;

        if (exceedsMaxLevel(event.getPokemon1(), player1)) {
            shouldCancel = true;
            String message = ConfigLoader.getTradeBlockedMessage();
            player1.sendMessage(new StringTextComponent(message), player1.getUUID());
        }
        if (exceedsMaxLevel(event.getPokemon2(), player2)) {
            shouldCancel = true;
            String message = ConfigLoader.getTradeBlockedMessage();
            player2.sendMessage(new StringTextComponent(message), player2.getUUID());
        }

        if (shouldCancel) {
            event.setCanceled(true);
        }
    }

    private boolean exceedsMaxLevel(Pokemon pokemon, ServerPlayerEntity player) {
        return pokemon != null && pokemon.getPokemonLevel() > BadgeUtils.getMaxLevelForPlayer(player);
    }
}