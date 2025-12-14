package tfar.inventorykeeper;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import tfar.inventorykeeper.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class InventoryKeeper {

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        Class<MenuType<?>> typeClass1 = (Class<MenuType<?>>) (Object) MenuType.class;

        Services.PLATFORM.registerAll(Init.class, BuiltInRegistries.MENU, typeClass1);
    }

    public static void clone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean wasDeath) {
        if (wasDeath) {
            copySavedInventories(oldPlayer, newPlayer);
        }
    }

    private static void copySavedInventories(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
        List<SavedInventory> oldSavedInventories = ((ServerPlayerDuck) oldPlayer).getSavedInventories();
        ((ServerPlayerDuck) newPlayer).setSavedInventories(oldSavedInventories);
    }

    public static void saveItems(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            Inventory inventory = player.getInventory();
            SavedInventory savedInventory = new SavedInventory();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty()) {
                    savedInventory.setItem(i, stack);
                    inventory.setItem(i, ItemStack.EMPTY);
                }
            }
            ((ServerPlayerDuck) player).getSavedInventories().add(savedInventory);
        }
    }

    public static void commands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(Constants.MOD_ID).requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("unlock")
                        .executes(InventoryKeeper::unlock)
                )
                .then(Commands.literal("restore")
                        .then(Commands.argument("index", IntegerArgumentType.integer(0))
                                .executes(InventoryKeeper::restore)
                        )
                )
        );
    }

    static int restore(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack commandSourceStack = context.getSource();
        ServerPlayer player = commandSourceStack.getPlayerOrException();
        int index = IntegerArgumentType.getInteger(context,"index");
        List<SavedInventory> savedInventoryList = ((ServerPlayerDuck)player).getSavedInventories();

        if (savedInventoryList.isEmpty()) {
            return 0;
        }

        if (index>=savedInventoryList.size()) return 0;
        restoreAndClose(player,savedInventoryList,index);
        return 1;
    }

    public static void restoreAndClose(ServerPlayer player,List<SavedInventory> savedInventories,int index) {
        SavedInventory savedInventory = savedInventories.get(index);
        if (player.containerMenu instanceof SavedInventoryMenu) {
            player.closeContainer();
        }

        savedInventory.restore(player);
        savedInventories.remove(index);
    }

    static int unlock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack commandSourceStack = context.getSource();
        ServerPlayer player = commandSourceStack.getPlayerOrException();
        List<SavedInventory> savedInventoryList = ((ServerPlayerDuck)player).getSavedInventories();

        if (savedInventoryList.isEmpty()) {
            return 0;
        }

        savedInventoryList.forEach(SavedInventory::open);

        return savedInventoryList.size();
    }

    public static ResourceLocation id(String packet) {
        return new ResourceLocation(Constants.MOD_ID, packet);
    }
}