package tfar.inventorykeeper.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import tfar.inventorykeeper.SavedInventory;
import tfar.inventorykeeper.SavedInventoryMenu;
import tfar.inventorykeeper.ServerPlayerDuck;
import tfar.inventorykeeper.platform.Services;

import java.util.List;
import java.util.Objects;

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
                    player.openMenu(new P(first,0));
                }
            }
        }
    }

    public static final class P implements MenuProvider {
        private final SavedInventory savedInventory;
        private final int index;

        public P(SavedInventory savedInventory, int index) {
            this.savedInventory = savedInventory;
            this.index = index;
        }


        @Override
            public Component getDisplayName() {
                return Component.literal("Death Inventory");
            }

            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new SavedInventoryMenu(i, inventory, index,savedInventory.dataSlot);
            }

        public SavedInventory savedInventory() {
            return savedInventory;
        }

        public int index() {
            return index;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (P) obj;
            return Objects.equals(this.savedInventory, that.savedInventory) &&
                    this.index == that.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(savedInventory, index);
        }

        @Override
        public String toString() {
            return "P[" +
                    "savedInventory=" + savedInventory + ", " +
                    "index=" + index + ']';
        }

        }

    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this);
    }

}
