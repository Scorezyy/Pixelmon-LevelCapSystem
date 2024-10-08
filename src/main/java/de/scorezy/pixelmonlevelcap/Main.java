package de.scorezy.pixelmonlevelcap;

import com.mojang.brigadier.CommandDispatcher;
import com.pixelmonmod.pixelmon.Pixelmon;
import de.scorezy.pixelmonlevelcap.commands.ReloadConfigCommand;
import de.scorezy.pixelmonlevelcap.listeners.*;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.command.CommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("pixelmonlevelcap")
public class Main {

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event) {
        ConfigLoader.loadConfig();

        Pixelmon.EVENT_BUS.register(new CaptureEventListener());
        Pixelmon.EVENT_BUS.register(new TradeEventListener());
        Pixelmon.EVENT_BUS.register(new PlayerInteractListener());
        Pixelmon.EVENT_BUS.register(new LevelUpEventListener());
        Pixelmon.EVENT_BUS.register(new RaidCaptureEventListener());
        Pixelmon.EVENT_BUS.register(new NPCTradeEventListener());
    }
    @Mod.EventBusSubscriber(modid = "pixelmonlevelcap", bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerEvents {
        @SubscribeEvent
        public static void onServerStarting(FMLServerStartedEvent event) {
            CommandDispatcher<CommandSource> dispatcher = event.getServer().getCommands().getDispatcher();
            ReloadConfigCommand.register(dispatcher);
        }
    }
}
