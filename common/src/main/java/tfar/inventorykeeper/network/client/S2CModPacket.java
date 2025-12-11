package tfar.inventorykeeper.network.client;


import tfar.inventorykeeper.network.ModPacket;

public interface S2CModPacket extends ModPacket {

    void handleClient();

}
