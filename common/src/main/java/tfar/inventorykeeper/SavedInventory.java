package tfar.inventorykeeper;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class SavedInventory implements Container{

    public final NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);
    public final NonNullList<ItemStack> armor = NonNullList.withSize(4, ItemStack.EMPTY);
    public final NonNullList<ItemStack> offhand = NonNullList.withSize(1, ItemStack.EMPTY);
    private final List<NonNullList<ItemStack>> compartments = ImmutableList.of(this.items, this.armor, this.offhand);
    boolean open;

    @Override
    public int getContainerSize() {
        return compartments.stream().mapToInt(NonNullList::size).sum();
    }

    @Override
    public boolean isEmpty() {
        return compartments.stream().flatMap(Collection::stream).allMatch(ItemStack::isEmpty);
    }

    public void open() {
        open = true;
    }


    public final DataSlot dataSlot = new DataSlot() {
        @Override
        public int get() {
            return open ? 1 : 0;
        }

        @Override
        public void set(int value) {
        }
    };

    /**
     * Returns the stack in the given slot.
     */
    @Override
    public ItemStack getItem(int index) {
        List<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : this.compartments) {
            if (index < nonnulllist.size()) {
                list = nonnulllist;
                break;
            }

            index -= nonnulllist.size();
        }

        return list == null ? ItemStack.EMPTY : list.get(index);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return null;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        NonNullList<ItemStack> nonnulllist = null;

        for (NonNullList<ItemStack> nonnulllist1 : this.compartments) {
            if (index < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }

            index -= nonnulllist1.size();
        }

        if (nonnulllist != null) {
            nonnulllist.set(index, stack);
        }
    }

    /**
     * Writes the inventory out as a list of compound tags. This is where the slot indices are used (+100 for armor, +80 for crafting).
     */
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (int i = 0; i < this.items.size(); i++) {
            if (!this.items.get(i).isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)i);
                listTag.add(this.items.get(i).save(compoundtag));
            }
        }

        for (int j = 0; j < this.armor.size(); j++) {
            if (!this.armor.get(j).isEmpty()) {
                CompoundTag compoundtag1 = new CompoundTag();
                compoundtag1.putByte("Slot", (byte)(j + 100));
                listTag.add(this.armor.get(j).save(compoundtag1));
            }
        }

        for (int k = 0; k < this.offhand.size(); k++) {
            if (!this.offhand.get(k).isEmpty()) {
                CompoundTag compoundtag2 = new CompoundTag();
                compoundtag2.putByte("Slot", (byte)(k + 150));
                listTag.add(this.offhand.get(k).save(compoundtag2));
            }
        }

        tag.put("items",listTag);
        tag.putBoolean("open", open);

        return tag;
    }

    /**
     * Reads from the given tag list and fills the slots in the inventory with the correct items.
     */
    public void load(CompoundTag tag) {
        ListTag listTag = tag.getList("items", ListTag.TAG_LIST);
        this.items.clear();
        this.armor.clear();
        this.offhand.clear();

        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundtag = listTag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.of(compoundtag);
            if (j < this.items.size()) {
                this.items.set(j, itemstack);
            } else if (j >= 100 && j < this.armor.size() + 100) {
                this.armor.set(j - 100, itemstack);
            } else if (j >= 150 && j < this.offhand.size() + 150) {
                this.offhand.set(j - 150, itemstack);
            }
        }
        open = tag.getBoolean("open");
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        this.compartments.forEach(NonNullList::clear);
    }

    public void restore(ServerPlayer player) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (player.getInventory().items.get(i).isEmpty()) {
                player.getInventory().items.set(i,stack);
            } else {
                player.drop(stack,false);
            }
        }

        for (int i = 0; i < armor.size(); i++) {
            ItemStack stack = armor.get(i);
            if (player.getInventory().armor.get(i).isEmpty()) {
                player.getInventory().armor.set(i,stack);
            } else {
                player.drop(stack,false);
            }
        }

        for (int i = 0; i < offhand.size(); i++) {
            ItemStack stack = offhand.get(i);
            if (player.getInventory().offhand.get(i).isEmpty()) {
                player.getInventory().offhand.set(i,stack);
            } else {
                player.drop(stack,false);
            }
        }
    }
}
