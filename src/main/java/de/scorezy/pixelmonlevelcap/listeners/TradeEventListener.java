package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TradeEventListener {

    @SubscribeEvent
    public void onPixelmonTrade(PixelmonTradeEvent.Pre event) {
        boolean shouldCancel = judgeCancel(event.getPlayer1(), event.getPokemon1()) ||
                judgeCancel(event.getPlayer2(), event.getPokemon2());

        if (shouldCancel) {
            event.setCanceled(true);
        }
    }

    private boolean judgeCancel(PlayerEntity player, Pokemon pokemon) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        if (exceedsMaxLevel(pokemon, serverPlayer)) {
            serverPlayer.sendMessage(
                    new TranslationTextComponent("pixelmonlevelcap.capped_msg.trade_blocked"),
                    serverPlayer.getUUID());
            return true;
        }
        return false;
    }

    private boolean exceedsMaxLevel(Pokemon pokemon, ServerPlayerEntity player) {
        return pokemon != null && pokemon.getPokemonLevel() > BadgeUtils.getLevelCapForPlayer(player);
    }
}
