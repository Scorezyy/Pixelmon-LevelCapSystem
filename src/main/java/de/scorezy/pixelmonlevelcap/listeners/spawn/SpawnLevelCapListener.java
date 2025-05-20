package de.scorezy.pixelmonlevelcap.listeners.spawn;

import com.pixelmonmod.pixelmon.api.storage.NPCPartyStorage;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import de.scorezy.pixelmonlevelcap.Main;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.List;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.GAME)
public class SpawnLevelCapListener {
    private static final String NBT_CLAMPED = "PixelmonLevelCap.Clamped";
    private static final double RADIUS = 6 * 16;

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (!ConfigLoader.isCapedPokeSpawnEnabled()) return;
        if (event.getLevel().isClientSide()) return;

        Entity ent = event.getEntity();
        if (!(ent instanceof PixelmonEntity pkm)) return;

        if (pkm.getPokemon().getSpecies().isLegendary() && !ConfigLoader.isLegendaryLevelCapEnabled()) {
            if (ConfigLoader.isDebugMessagesEnabled()) {
                System.out.println("[SpawnClamp] Skipping legendary " + pkm.getPokemonName());
            }
            return;
        }

        if (pkm.getOwner() != null
                || pkm.getPokemon().getOwnerPlayer() != null
                || pkm.getStorage() != null && pkm.getStorage() instanceof NPCPartyStorage npcPartyStorage && npcPartyStorage.getNPC() != null) {
            return;
        }

        CompoundTag data = pkm.getPersistentData();
        if (data.getBoolean(NBT_CLAMPED)) return;

        ServerLevel world = (ServerLevel) pkm.level();
        List<ServerPlayer> nearby = world.players().stream()
                .filter(pl -> pl instanceof ServerPlayer)
                .map(pl -> (ServerPlayer) pl)
                .filter(pl -> pl.distanceTo(pkm) <= RADIUS)
                .toList();

        int minCap = nearby.stream()
                .mapToInt(BadgeUtils::getMaxLevelForPlayer)
                .min()
                .orElse(1);

        int orig = pkm.getLvl().getPokemonLevel();
        int clamped = Math.min(Math.max(1, orig), minCap);

        if (ConfigLoader.isDebugMessagesEnabled()) {
            System.out.println("[SpawnClamp] "
                    + pkm.getPokemonName()
                    + " orig=" + orig
                    + " -> setLevel=" + clamped
                    + " (minCap=" + minCap
                    + ", playersInRange=" + nearby.size() + ")");
        }

        pkm.getLvl().setLevel(clamped);
        data.putBoolean(NBT_CLAMPED, true);
    }
}
