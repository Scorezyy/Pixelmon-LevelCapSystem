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
        Pokemon pokemon1 = event.getPokemon1();
        Pokemon pokemon2 = event.getPokemon2();
        boolean shouldCancel = false;

        if (pokemon1 != null && exceedsMaxLevel(pokemon1, player1)) {
            shouldCancel = true;
            player1.sendMessage(
                    new StringTextComponent(ConfigLoader.getTradeBlockedMessage()),
                    player1.getUUID()
            );
        }
        if (pokemon2 != null && exceedsMaxLevel(pokemon2, player2)) {
            shouldCancel = true;
            player2.sendMessage(
                    new StringTextComponent(ConfigLoader.getTradeBlockedMessage()),
                    player2.getUUID()
            );
        }

        if (pokemon1 != null && exceedsMaxLevel(pokemon1, player2)) {
            shouldCancel = true;
            player2.sendMessage(
                    new StringTextComponent(ConfigLoader.getTradeBlockedPartnerMessage()),
                    player2.getUUID()
            );
        }
        if (pokemon2 != null && exceedsMaxLevel(pokemon2, player1)) {
            shouldCancel = true;
            player1.sendMessage(
                    new StringTextComponent(ConfigLoader.getTradeBlockedPartnerMessage()),
                    player1.getUUID()
            );
        }

        if (shouldCancel) {
            event.setCanceled(true);
        }
    }

    private boolean exceedsMaxLevel(Pokemon pokemon, ServerPlayerEntity player) {
        return pokemon.getPokemonLevel() > BadgeUtils.getMaxLevelForPlayer(player);
    }
}
