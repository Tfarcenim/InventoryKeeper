package tfar.inventorykeeper.network;

import net.minecraft.network.FriendlyByteBuf;

public interface ModPacket {
    void write(FriendlyByteBuf to);

}
