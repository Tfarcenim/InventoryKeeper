package tfar.inventorykeeper.network;

import net.minecraft.resources.ResourceLocation;
import tfar.inventorykeeper.network.server.C2SButtonPacket;
import tfar.inventorykeeper.platform.Services;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {

        Services.PLATFORM.registerServerPacket(C2SButtonPacket.class, C2SButtonPacket::fromPacket);

        ///////server to client
    }

}
