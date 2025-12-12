package tfar.inventorykeeper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import tfar.inventorykeeper.network.server.C2SButtonPacket;

import java.util.List;

public class SavedInventoryMenu extends AbstractContainerMenu {

    public final DataSlot dataSlot = DataSlot.standalone();

    protected SavedInventoryMenu(int containerId, Inventory playerInventory) {
        this(containerId,playerInventory,new SimpleContainer(41),0);
    }

    public SavedInventoryMenu(int containerId, Inventory playerInventory, Container savedInventory,int index) {
        super(Init.MENU, containerId);

        for(int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslot = InventoryMenu.SLOT_IDS[k];
            this.addSlot(new Slot(savedInventory, 39 - k, 8, 8 + k * 18) {

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(ItemStack p_39746_) {
                    return false;
                }

                @Override
                public boolean mayPickup(Player p_39744_) {
                    return false;
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
                }
            });
            addDataSlot(dataSlot);
            dataSlot.set(index);
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(savedInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18){
                    @Override
                    public boolean mayPlace(ItemStack p_39746_) {
                        return false;
                    }

                    @Override
                    public boolean mayPickup(Player p_39744_) {
                        return false;
                    }
                });
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(savedInventory, i1, 8 + i1 * 18, 142){
                @Override
                public boolean mayPlace(ItemStack p_39746_) {
                    return false;
                }

                @Override
                public boolean mayPickup(Player p_39744_) {
                    return false;
                }
            });
        }

        this.addSlot(new Slot(savedInventory, 40, 77, 62) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
            @Override
            public boolean mayPlace(ItemStack p_39746_) {
                return false;
            }

            @Override
            public boolean mayPickup(Player p_39744_) {
                return false;
            }
        });

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
            }
            player.openMenu(new C2SButtonPacket.P(savedInventoryList.get(index),index));
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
        LEFT,RIGHT
    }
}
