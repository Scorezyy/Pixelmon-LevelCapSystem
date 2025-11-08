package de.scorezy.pixelmonlevelcapsystem;

import com.mojang.brigadier.CommandDispatcher;
import com.pixelmonmod.pixelmon.Pixelmon;
import de.scorezy.pixelmonlevelcapsystem.commands.ReloadConfigCommand;
import de.scorezy.pixelmonlevelcapsystem.listeners.*;
import de.scorezy.pixelmonlevelcapsystem.listeners.spawn.SpawnLevelCapListener;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.StartScreen;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;


@Mod("pixelmonlevelcapsystem")
public class Main {

    public Main(IEventBus modEventBus) {
        modEventBus.addListener(this::setup);
        NeoForge.EVENT_BUS.register(ServerEvents.class);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ConfigLoader.loadConfig();

            Pixelmon.EVENT_BUS.register(new CaptureEventListener());
            Pixelmon.EVENT_BUS.register(new TradeEventListener());
            Pixelmon.EVENT_BUS.register(new ExperienceListener());
            Pixelmon.EVENT_BUS.register(new RaidCaptureEventListener());
            Pixelmon.EVENT_BUS.register(new NPCTradeEventListener());
            Pixelmon.EVENT_BUS.register(new SpawnLevelCapListener());
            Pixelmon.EVENT_BUS.register(new DuplicatedBadgeListener());
        });
    }

    public static class ServerEvents {
        @SubscribeEvent
        public static void onServerStarting(ServerStartedEvent event) {
            StartScreen.printStartupBanner(event.getServer());

            CommandDispatcher<CommandSourceStack> dispatcher = event.getServer().getCommands().getDispatcher();
            ReloadConfigCommand.register(dispatcher);
        }
    }

}
