package tfar.inventorykeeper.network.server;

import net.minecraft.server.level.ServerPlayer;
import tfar.inventorykeeper.network.ModPacket;

public interface C2SModPacket extends ModPacket {

    void handleServer(ServerPlayer player);

}
