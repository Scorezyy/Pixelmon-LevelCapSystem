package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TradeEventListener {

    @SubscribeEvent
    public void onPixelmonTrade(PixelmonTradeEvent.Pre event) {
        ServerPlayerEntity player1 = (ServerPlayerEntity) event.getPlayer1();
        ServerPlayerEntity player2 = (ServerPlayerEntity) event.getPlayer2();
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

    private boolean checkAndNotify(Pokemon pokemon, ServerPlayerEntity owner, ServerPlayerEntity partner) {
        if (pokemon == null) {
            return false;
        }

        int level      = pokemon.getPokemonLevel();
        int ownerCap   = BadgeUtils.getMaxLevelForPlayer(owner);
        int partnerCap = BadgeUtils.getMaxLevelForPlayer(partner);

        if (level > ownerCap) {
            owner.sendMessage(new StringTextComponent(ConfigLoader.getTradeBlockedMessage()), Util.NIL_UUID);
            return true;
        }

        if (level > partnerCap) {
            partner.sendMessage(new StringTextComponent(ConfigLoader.getTradeBlockedMessage()), Util.NIL_UUID);
            owner.sendMessage(new StringTextComponent(ConfigLoader.getTradeBlockedPartnerMessage()), Util.NIL_UUID);
            return true;
        }

        return false;
    }
}
