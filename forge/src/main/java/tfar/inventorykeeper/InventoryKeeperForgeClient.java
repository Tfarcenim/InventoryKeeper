package tfar.inventorykeeper;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tfar.inventorykeeper.network.server.C2SButtonPacket;

public class InventoryKeeperForgeClient {
    static void setup(FMLClientSetupEvent event) {
        MenuScreens.register(Init.MENU,SavedInventoryScreen::new);
        MinecraftForge.EVENT_BUS.addListener(InventoryKeeperForgeClient::addButton);
    }

    static void addButton(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen instanceof InventoryScreen inventoryScreen) {
            ImageButton imageButton;
            Button button = Button.builder(Component.literal("\uD83D\uDC80"),button1 -> {
                C2SButtonPacket.OPEN.send();
            }).bounds(inventoryScreen.getGuiLeft()+75,inventoryScreen.getGuiTop()+40,20,20).build();
            event.addListener(button);
        }
    }

    public static void init(IEventBus bus) {
        bus.addListener(InventoryKeeperForgeClient::setup);
    }
}
