package tfar.inventorykeeper;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class Init {
    public static final MenuType<SavedInventoryMenu> MENU = new MenuType<>(SavedInventoryMenu::new, FeatureFlags.VANILLA_SET);
}
