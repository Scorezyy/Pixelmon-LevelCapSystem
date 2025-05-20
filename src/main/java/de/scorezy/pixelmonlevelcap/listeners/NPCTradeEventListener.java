package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.npc.NPCTraderEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

public class NPCTradeEventListener {

    @SubscribeEvent
    public void onNPCTrade(NPCTraderEvent.AcceptTrade event) {
        Pokemon tradedPokemon = event.getTradedPokemon();
        if (tradedPokemon == null) {
            return;
        }

        ServerPlayer player = (ServerPlayer) event.getPlayer();
        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);
        if (tradedPokemon.getPokemonLevel() <= maxLevel) {
            return;
        }

        tradedPokemon.setLevel(maxLevel);
        String message = ConfigLoader.getNPCTradeAccessMessage();
        player.sendSystemMessage(Component.literal(message));
    }
}