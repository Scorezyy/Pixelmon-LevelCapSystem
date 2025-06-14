package de.scorezy.pixelmonlevelcapsystem.listeners.spawn;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import de.scorezy.pixelmonlevelcapsystem.utils.BadgeUtils;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.Logger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = "pixelmonlevelcapsystem", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnLevelCapListener {

    private static final String NBT_CLAMPED = "PixelmonLevelCap.Cap";
    private static final double RADIUS = 6 * 16;

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!ConfigLoader.getSettingsConfig().isLevelCapWildPokemons()) {
            return;
        }

        Entity ent = event.getEntity();
        if (!(ent instanceof PixelmonEntity)) {
            return;
        }

        PixelmonEntity pkm = (PixelmonEntity) ent;
        String speciesName = pkm.getPokemonName();

        if (pkm.getPokemon().getSpecies().isLegendary()
                && !ConfigLoader.getSettingsConfig().isLevelCapLegendaryPokemons()) {
            if (ConfigLoader.getSettingsConfig().isDebug()) {
                Logger.debug("Skipping legendary " + speciesName);
            }
            return;
        }

        if (pkm.getOwner() != null
                || pkm.getPokemon().getOwnerPlayer() != null
                || pkm.getPokemon().getOwnerTrainer() != null) {
            return;
        }

        CompoundNBT data = pkm.getPersistentData();
        if (data.getBoolean(NBT_CLAMPED)) {
            return;
        }

        if (ConfigLoader.getExcludeLevelCapPokemonConfig().isExcluded(speciesName)) {
            if (ConfigLoader.getSettingsConfig().isDebug()) {
                Logger.debug("Skipping excluded Pokémon " + speciesName);
            }
            return;
        }

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

        int origLevel = pkm.getLvl().getPokemonLevel();
        int clamped = Math.min(Math.max(1, origLevel), minCap);

        if (ConfigLoader.getSettingsConfig().isDebug()) {
            Logger.debug(String.format("%s origLevel=%d -> setLevel=%d (BadgeLevelCap=%d, playersInRange=%d)",
                    speciesName, origLevel, clamped, minCap, nearby.size()));
        }

        pkm.getLvl().setLevel(clamped);
        data.putBoolean(NBT_CLAMPED, true);
    }
}
