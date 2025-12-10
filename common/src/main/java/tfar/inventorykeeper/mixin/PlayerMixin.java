package tfar.inventorykeeper.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.inventorykeeper.InventoryKeeper;
import tfar.inventorykeeper.SavedInventory;
import tfar.inventorykeeper.ServerPlayerDuck;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements ServerPlayerDuck {

    SavedInventory savedInventory = new SavedInventory();


    @Inject(method = "dropEquipment",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;destroyVanishingCursedItems()V"))
    private void saveBedItems(CallbackInfo ci) {
        InventoryKeeper.saveItems((Player)(Object) this);
    }

    @Inject(method = "addAdditionalSaveData",at = @At("HEAD"))
    private void addExtraData(CompoundTag compound, CallbackInfo ci) {
        compound.put("SavedInventory", savedInventory.save(new ListTag()));
    }

    @Inject(method = "readAdditionalSaveData",at = @At("HEAD"))
    private void readExtraData(CompoundTag compound, CallbackInfo ci) {
        ListTag listtag = compound.getList("SavedInventory", Tag.TAG_COMPOUND);
        savedInventory.load(listtag);
    }

    @Override
    public SavedInventory getSavedInventory() {
        return savedInventory;
    }

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }


}