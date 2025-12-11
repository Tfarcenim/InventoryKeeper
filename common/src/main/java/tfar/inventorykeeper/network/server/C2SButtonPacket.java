package tfar.inventorykeeper.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tfar.inventorykeeper.SavedInventory;
import tfar.inventorykeeper.ServerPlayerDuck;
import tfar.inventorykeeper.platform.Services;

import java.util.List;

public enum C2SButtonPacket implements C2SModPacket {
    OPEN;

    public static C2SButtonPacket fromPacket(FriendlyByteBuf buf) {
        return buf.readEnum(C2SButtonPacket.class);
    }

    public void send() {
        Services.PLATFORM.sendToServer(this);
    }

    public void handleServer(ServerPlayer player) {
        switch (this) {
            case OPEN -> {
                List<SavedInventory> savedInventoryList = ((ServerPlayerDuck)player).getSavedInventories();
                if (!savedInventoryList.isEmpty()) {
                    SavedInventory first = savedInventoryList.get(0);
                    player.openMenu(first);
                }
            }
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this);
    }

}
