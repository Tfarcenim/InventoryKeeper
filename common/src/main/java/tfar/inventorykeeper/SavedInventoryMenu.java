package tfar.inventorykeeper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SavedInventoryMenu extends AbstractContainerMenu {

    public final DataSlot dataSlot = DataSlot.standalone();
    public final DataSlot open;
    public final Player player;

    protected SavedInventoryMenu(int containerId, Inventory playerInventory) {
        this(containerId,playerInventory,0,DataSlot.standalone());
    }

    public SavedInventoryMenu(int containerId, Inventory playerInventory,int index,DataSlot open) {
        super(Init.MENU, containerId);

        for(int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslot = InventoryMenu.SLOT_IDS[k];
            this.addSlot(new ASlot(this, 39 - k, 8, 8 + k * 18) {

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
                }
            });
        }
        this.open = open;
        addDataSlot(dataSlot);
        addDataSlot(open);
        dataSlot.set(index);

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new ASlot(this, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new ASlot(this, i1, 8 + i1 * 18, 142));
        }

        this.addSlot(new ASlot(this, 40, 77, 62) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
        player = playerInventory.player;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id < 0 || id >= Action.values().length) return false;
        Action action = Action.values()[id];
        if (player instanceof ServerPlayer serverPlayer) {
            int index = dataSlot.get();
            List<SavedInventory> savedInventoryList = ((ServerPlayerDuck)player).getSavedInventories();
            switch (action) {
                case LEFT -> {
                    if (index > 0) {
                        index--;
                    }
                }
                case RIGHT -> {
                    if (index < savedInventoryList.size() - 1) {
                        index++;
                    }
                }
                case RESTORE -> {
                    SavedInventory savedInventory = savedInventoryList.get(index);
                    if (savedInventory.open) {
                        InventoryKeeper.restoreAndClose(serverPlayer,savedInventoryList,index);
                    }
                    return true;
                }
            }
            dataSlot.set(index);
        }
        return true;
    }


    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public enum Action {
        LEFT,RIGHT,RESTORE
    }
}
