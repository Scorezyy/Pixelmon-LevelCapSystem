package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.npc.NPCTraderEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NPCTradeEventListener {

    @SubscribeEvent
    public void onNPCTrade(NPCTraderEvent.AcceptTrade event) {
        Pokemon tradedPokemon = event.getTradedPokemon();
        if (tradedPokemon == null) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        int maxLevel = BadgeUtils.getMaxLevelForPlayer(player);
        if (tradedPokemon.getPokemonLevel() <= maxLevel) {
            return;
        }

        tradedPokemon.setLevel(maxLevel);
        String message = ConfigLoader.getNPCTradeAccessMessage();
        player.sendMessage(new StringTextComponent(message), player.getUUID());
    }
}