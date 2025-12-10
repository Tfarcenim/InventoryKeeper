package tfar.inventorykeeper;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class InventoryKeeperForge {
    
    public InventoryKeeperForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        InventoryKeeper.init();
        MinecraftForge.EVENT_BUS.addListener(this::playerClone);
    }

    void playerClone(PlayerEvent.Clone event) {
        InventoryKeeper.clone((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getEntity(),event.isWasDeath());
    }
}
//'m looking to commission an alternative keep inventory style mod,
//
//Premise: When the player dies their inventory state is moved into a GUI accessible from an icon within the inventory, the items are unobtainable until 1 of 2 conditions are met
//
//
//An access item is used, IE: humanity from dark souls
//The access command is used [Integration for Scripts]
//
//Requirements:
//Version: 1.20.1
//Platform: Forge (47.4.10 if it matters)
//
//   Multiple Deaths: The Inventory must be able to handle multiple deaths in a row, Multiple Tabs or Expanding Inventory slots, something like that
//
//   Curios: Curios items must either stay on the player after death or be taken into the death inventory, preferably they stay with the player so if the death inventory somehow bypasses keepinventory that would work
//
//Bonus $ (TBD after Core is completed):
//
//   Toggleable Configuration to grant access to items after slaying the mob that killed the player (if the player died to a mob)
//
//Let me know if you're interested or have questions