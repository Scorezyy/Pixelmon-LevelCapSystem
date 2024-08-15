package de.scorezy.pixelmonlevelcap.listeners;

import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;

public class TradeEventListener {

    @SubscribeEvent
    public void onPixelmonTrade(PixelmonTradeEvent.Pre event) {
        ServerPlayerEntity player1 = (ServerPlayerEntity) event.getPlayer1();
        ServerPlayerEntity player2 = (ServerPlayerEntity) event.getPlayer2();

        if (player1 != null && player2 != null) {
            int maxLevelPlayer1 = BadgeUtils.getMaxLevelForPlayer(player1);
            int maxLevelPlayer2 = BadgeUtils.getMaxLevelForPlayer(player2);
            boolean shouldCancel = false;
            String message = "";

            if (event.getPokemon1() != null && event.getPokemon1().getPokemonLevel() > maxLevelPlayer1) {
                shouldCancel = true;
                message = "Du kannst dein Pokémon nicht tauschen, da es das erlaubte Level überschreitet!";
            }
            if (event.getPokemon2() != null && event.getPokemon2().getPokemonLevel() > maxLevelPlayer2) {
                shouldCancel = true;
                message = "§cDu kannst dein §ePokémon §cnicht tauschen, da es das erlaubte Level überschreitet!";
            }

            if (shouldCancel) {
                event.setCanceled(true);
                if (!message.isEmpty()) {
                    player1.sendMessage(new StringTextComponent(message), player1.getUUID());
                    player2.sendMessage(new StringTextComponent(message), player2.getUUID());
                }
            }
        }
    }
}