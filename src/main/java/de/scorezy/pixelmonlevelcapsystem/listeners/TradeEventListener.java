package de.scorezy.pixelmonlevelcapsystem.listeners;

import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.scorezy.pixelmonlevelcapsystem.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TradeEventListener {

    @SubscribeEvent
    public void onPixelmonTrade(PixelmonTradeEvent.Pre event) {
        if (!ConfigLoader.getSettingsConfig().isLevelCapPlayerTrades()) {
            return;
        }

        ServerPlayerEntity p1 = (ServerPlayerEntity) event.getPlayer1();
        ServerPlayerEntity p2 = (ServerPlayerEntity) event.getPlayer2();

        if (attemptBlock(event.getPokemon1(), p1, p2) || attemptBlock(event.getPokemon2(), p2, p1)) {
            event.setCanceled(true);
        }
    }

    private boolean attemptBlock(Pokemon pokemon, ServerPlayerEntity owner, ServerPlayerEntity partner) {
        if (pokemon == null) return false;

        boolean debug       = ConfigLoader.getSettingsConfig().isDebug();
        String ownerName    = owner.getName().getString();
        String partnerName  = partner.getName().getString();
        String species      = pokemon.getSpecies().getName();
        int level           = pokemon.getPokemonLevel();
        int ownerCap        = BadgeUtils.getMaxLevelForPlayer(owner);
        int partnerCap      = BadgeUtils.getMaxLevelForPlayer(partner);

        boolean ownerTooHigh   = level > ownerCap;
        boolean partnerTooHigh = level > partnerCap;

        if (ownerTooHigh || partnerTooHigh) {
            String reason  = partnerTooHigh ? "partner over cap" : "owner over cap";

            if (debug) {
                Logger.debug(String.format(
                        "%s (cap %d) attempted to trade %s (lvl %d) to %s (cap %d) – blocked (%s)",
                        ownerName, ownerCap, species, level, partnerName, partnerCap, reason
                ));
            }

            partner.sendMessage(
                    new StringTextComponent(ConfigLoader.getMessagesConfig().getTradeBlocked()),
                    Util.NIL_UUID
            );
            owner.sendMessage(
                    new StringTextComponent(ConfigLoader.getMessagesConfig().getTradeBlockedPartner()),
                    Util.NIL_UUID
            );

            return true;
        }

        if (debug) {
            Logger.debug(String.format(
                    "%s (cap %d) traded %s (lvl %d) to %s (cap %d) successfully",
                    ownerName, ownerCap, species, level, partnerName, partnerCap
            ));
        }

        return false;
    }
}
