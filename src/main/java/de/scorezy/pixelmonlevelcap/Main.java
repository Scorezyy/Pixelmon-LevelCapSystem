package de.scorezy.pixelmonlevelcap;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import com.pixelmonmod.pixelmon.Pixelmon;
import de.scorezy.pixelmonlevelcap.commands.ReloadConfigCommand;
import de.scorezy.pixelmonlevelcap.listeners.CaptureEventListener;
import de.scorezy.pixelmonlevelcap.listeners.TradeEventListener;
import de.scorezy.pixelmonlevelcap.listeners.PlayerInteractListener;
import de.scorezy.pixelmonlevelcap.listeners.LevelUpEventListener;
import de.scorezy.pixelmonlevelcap.listeners.RaidCaptureEventListener;
import de.scorezy.pixelmonlevelcap.listeners.NPCTradeEventListener;
import de.scorezy.pixelmonlevelcap.listeners.spawn.SpawnLevelCapListener;
import de.scorezy.pixelmonlevelcap.utils.ConfigLoader;
import net.minecraft.commands.CommandSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.slf4j.Logger;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "pixelmonlevelcap";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::setup);
        NeoForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ConfigLoader.loadConfig();

        Pixelmon.EVENT_BUS.register(new CaptureEventListener());
        Pixelmon.EVENT_BUS.register(new TradeEventListener());
        Pixelmon.EVENT_BUS.register(new PlayerInteractListener());
        Pixelmon.EVENT_BUS.register(new LevelUpEventListener());
        Pixelmon.EVENT_BUS.register(new RaidCaptureEventListener());
        Pixelmon.EVENT_BUS.register(new NPCTradeEventListener());
        Pixelmon.EVENT_BUS.register(new SpawnLevelCapListener());
    }

    @EventBusSubscriber(modid = "pixelmonlevelcap")
    public static class ServerEvents {
        @SubscribeEvent
        public static void onServerStarting(ServerStartedEvent event) {
            CommandDispatcher<CommandSource> dispatcher = event.getServer().getCommands().getDispatcher();
            ReloadConfigCommand.register(dispatcher);
        }
    }
}
