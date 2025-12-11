package tfar.inventorykeeper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class SavedInventoryMenu extends AbstractContainerMenu {

    protected SavedInventoryMenu(int containerId, Inventory playerInventory) {
        this(containerId,playerInventory,new SimpleContainer(36));
    }

    protected SavedInventoryMenu(int containerId, Inventory playerInventory, Container savedInventory) {
        super(Init.MENU, containerId);

        for(int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslot = InventoryMenu.SLOT_IDS[k];
            this.addSlot(new Slot(savedInventory, 39 - k, 8, 8 + k * 18) {

                public int getMaxStackSize() {
                    return 1;
                }

                public boolean mayPlace(ItemStack p_39746_) {
                    return false;
                }

                public boolean mayPickup(Player p_39744_) {
                    return false;
                }

                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
                }
            });
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(savedInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(savedInventory, i1, 8 + i1 * 18, 142));
        }

        this.addSlot(new Slot(savedInventory, 40, 77, 62) {
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });

    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
