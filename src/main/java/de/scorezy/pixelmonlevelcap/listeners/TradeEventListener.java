package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;


public class TradeEventListener {

    @SubscribeEvent
    public void onPixelmonTrade(PixelmonTradeEvent.Pre event) {
        ServerPlayer player1 = (ServerPlayer) event.getPlayer1();
        ServerPlayer player2 = (ServerPlayer) event.getPlayer2();
        Pokemon poke1 = event.getPokemon1();
        Pokemon poke2 = event.getPokemon2();

        if (checkAndNotify(poke1, player1, player2)) {
            event.setCanceled(true);
            return;
        }
        if (checkAndNotify(poke2, player2, player1)) {
            event.setCanceled(true);
        }
    }

    private boolean checkAndNotify(Pokemon pokemon, ServerPlayer owner, ServerPlayer partner) {
        if (pokemon == null) {
            return false;
        }

        int level      = pokemon.getPokemonLevel();
        int ownerCap   = BadgeUtils.getMaxLevelForPlayer(owner);
        int partnerCap = BadgeUtils.getMaxLevelForPlayer(partner);

        if (level > ownerCap) {
            owner.sendSystemMessage(Component.literal(ConfigLoader.getTradeBlockedMessage()));
            return true;
        }

        if (level > partnerCap) {
            partner.sendSystemMessage(Component.literal(ConfigLoader.getTradeBlockedMessage()));
            owner.sendSystemMessage(Component.literal(ConfigLoader.getTradeBlockedPartnerMessage()));
            return true;
        }

        return false;
    }
}
