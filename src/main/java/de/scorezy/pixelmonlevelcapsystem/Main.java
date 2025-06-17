package de.scorezy.pixelmonlevelcapsystem;

import com.mojang.brigadier.CommandDispatcher;
import com.pixelmonmod.pixelmon.Pixelmon;
import de.scorezy.pixelmonlevelcapsystem.commands.ReloadConfigCommand;
import de.scorezy.pixelmonlevelcapsystem.listeners.*;
import de.scorezy.pixelmonlevelcapsystem.listeners.spawn.SpawnLevelCapListener;
import de.scorezy.pixelmonlevelcapsystem.utils.ConfigLoader;
import de.scorezy.pixelmonlevelcapsystem.utils.PermissionHandler;
import de.scorezy.pixelmonlevelcapsystem.utils.StartScreen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("pixelmonlevelcapsystem")
public class Main {

    public Main() {
        FMLJavaModLoadingContext.get()
                .getModEventBus()
                .addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ConfigLoader.loadConfig();

        Pixelmon.EVENT_BUS.register(new CaptureEventListener());
        Pixelmon.EVENT_BUS.register(new TradeEventListener());
        Pixelmon.EVENT_BUS.register(new ExperienceListener());
        Pixelmon.EVENT_BUS.register(new RaidCaptureEventListener());
        Pixelmon.EVENT_BUS.register(new NPCTradeEventListener());
        Pixelmon.EVENT_BUS.register(new SpawnLevelCapListener());
        Pixelmon.EVENT_BUS.register(new DuplicatedBadgeListener());
    }

    @SubscribeEvent
    public void onServerStarted(final ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        StartScreen.printStartupBanner(server);
    }

    @SubscribeEvent
    public void onRegisterCommands(final RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        ReloadConfigCommand.register(dispatcher);
    }
}