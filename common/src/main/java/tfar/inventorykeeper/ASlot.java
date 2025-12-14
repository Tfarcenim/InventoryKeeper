package tfar.inventorykeeper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class ASlot extends Slot {
    private final SavedInventoryMenu menu;
    private final Container dummy;

    public ASlot(SavedInventoryMenu menu, int slot, int x, int y) {
        super(new SimpleContainer(0), slot, x, y);
        this.menu = menu;
        dummy = new SimpleContainer(41);
    }

    Container getContainer() {
        if (menu.player.level().isClientSide) {
            return dummy;
        }
        int index = menu.dataSlot.get();
        List<SavedInventory> savedInventories = ((ServerPlayerDuck) menu.player).getSavedInventories();
        index = Math.min(index,savedInventories.size() - 1);
        return savedInventories.get(index);
    }

    public ItemStack getItem() {
        return this.getContainer().getItem(this.getContainerSlot());
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    /**
     * Helper method to put a stack in the slot.
     */
    public void set(ItemStack stack) {
        this.getContainer().setItem(getContainerSlot(), stack);
        this.setChanged();
    }

    public void setChanged() {
        this.getContainer().setChanged();
    }

    public int getMaxStackSize() {
        return this.getContainer().getMaxStackSize();
    }

    public int getMaxStackSize(ItemStack stack) {
        return Math.min(this.getMaxStackSize(), stack.getMaxStackSize());
    }

    @Nullable
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return null;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
     */
    public ItemStack remove(int amount) {
        return this.getContainer().removeItem(getContainerSlot(), amount);
    }

}
