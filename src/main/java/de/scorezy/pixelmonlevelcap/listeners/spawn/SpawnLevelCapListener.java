package de.scorezy.pixelmonlevelcap.listeners.spawn;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import de.scorezy.pixelmonlevelcap.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = "pixelmonlevelcap", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnLevelCapListener {
    private static final String NBT_CLAMPED = "PixelmonLevelCap.Clamped";
    private static final double RADIUS = 6 * 16;

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!ConfigLoader.isCapedPokeSpawnEnabled()) return;
        if (event.getWorld().isClientSide()) return;

        Entity ent = event.getEntity();
        if (!(ent instanceof PixelmonEntity)) return;
        PixelmonEntity pkm = (PixelmonEntity) ent;

        if (pkm.getPokemon().getSpecies().isLegendary() && !ConfigLoader.isLegendaryLevelCapEnabled()) {
            if (ConfigLoader.isDebugMessagesEnabled()) {
                System.out.println("[SpawnClamp] Skipping legendary " + pkm.getPokemonName());
            }
            return;
        }

        if (pkm.getOwner() != null
                || pkm.getPokemon().getOwnerPlayer() != null
                || pkm.getPokemon().getOwnerTrainer() != null) {
            return;
        }

        CompoundNBT data = pkm.getPersistentData();
        if (data.getBoolean(NBT_CLAMPED)) return;

        ServerWorld world = (ServerWorld) pkm.level;
        List<ServerPlayerEntity> nearby = world.players().stream()
                .filter(pl -> pl instanceof ServerPlayerEntity)
                .map(pl -> (ServerPlayerEntity) pl)
                .filter(pl -> pl.distanceTo(pkm) <= RADIUS)
                .collect(Collectors.toList());

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
