package tfar.inventorykeeper;

import java.util.List;

public interface ServerPlayerDuck {

    List<SavedInventory> getSavedInventories();
    void setSavedInventories(List<SavedInventory> savedInventories);
}
