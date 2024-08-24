package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.npc.NPCTraderEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NPCTradeEventListener {

    @SubscribeEvent
    public void onNPCTrade(NPCTraderEvent.AcceptTrade event) {
        Pokemon tradedPokemon = event.getTradedPokemon();
        if (tradedPokemon == null) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        int maxLevel = BadgeUtils.getLevelCapForPlayer(player);
        if (tradedPokemon.getPokemonLevel() <= maxLevel) {
            return;
        }

        tradedPokemon.setLevel(maxLevel);
        player.sendMessage(
                new TranslationTextComponent("pixelmonlevelcap.capped_msg.npc_trade_access"),
                player.getUUID());
    }
}