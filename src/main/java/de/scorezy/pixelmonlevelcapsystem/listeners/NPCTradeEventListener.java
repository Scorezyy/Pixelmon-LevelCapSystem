package de.scorezy.pixelmonlevelcapsystem.listeners;

import com.pixelmonmod.pixelmon.api.events.npc.NPCTraderEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcapsystem.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

public class NPCTradeEventListener {

    @SubscribeEvent
    public void onNPCTrade(NPCTraderEvent.AcceptTrade event) {
        if (!ConfigLoader.getSettingsConfig().isLevelCapNpcTrades()) {
            return;
        }

        Pokemon tradedPokemon = event.getTradedPokemon();
        if (tradedPokemon == null) {
            return;
        }

        ServerPlayer player = (ServerPlayer) event.getPlayer();
        String playerName = player.getName().getString();
        String speciesName = tradedPokemon.getSpecies().getName();
        int originalLevel = tradedPokemon.getPokemonLevel();
        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);

        if (originalLevel <= maxLevel) {
            Logger.debug(playerName + " received " + speciesName +
                    " (lvl " + originalLevel + ") from NPC (allowed, cap " + maxLevel + ")");
            return;
        }

        Logger.debug(playerName + " attempted to receive " + speciesName +
                " (lvl " + originalLevel + ") from NPC (levelcap to " + maxLevel + ")");
        tradedPokemon.setLevel(maxLevel);
        String message = ConfigLoader.getMessagesConfig().getNpcTradeAccess();
        player.sendSystemMessage(Component.literal(message));
    }
}
