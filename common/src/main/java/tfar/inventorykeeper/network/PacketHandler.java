package tfar.inventorykeeper.network;

import tfar.inventorykeeper.network.server.C2SButtonPacket;
import tfar.inventorykeeper.platform.Services;

public class PacketHandler {

    public static void registerPackets() {

        Services.PLATFORM.registerServerPacket(C2SButtonPacket.class, C2SButtonPacket::fromPacket);

        ///////server to client
    }

}
