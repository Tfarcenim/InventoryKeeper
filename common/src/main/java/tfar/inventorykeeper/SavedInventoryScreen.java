package tfar.inventorykeeper;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SavedInventoryScreen extends AbstractContainerScreen<SavedInventoryMenu> {
    private static final ResourceLocation TEXTURE = InventoryKeeper.id("textures/gui/inventory.png");
    public SavedInventoryScreen(SavedInventoryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        titleLabelX+=69;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        button.active = menu.open.get()>0;
    }

    protected Button button;

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.literal("<"),button -> pressLeft()).bounds(leftPos+100,topPos+16,20,20).build());
        addRenderableWidget(Button.builder(Component.literal(">"),button -> pressRight()).bounds(leftPos+130,topPos+16,20,20).build());
        button = Button.builder(Component.literal("Restore"),button -> restoreInventory()).bounds(leftPos+100,topPos+40,50,20).build();
        addRenderableWidget(button);
    }

    void pressLeft() {
        sendButtonToServer(SavedInventoryMenu.Action.LEFT);
    }

    void pressRight() {
        sendButtonToServer(SavedInventoryMenu.Action.RIGHT);
    }

    void restoreInventory() {
        sendButtonToServer(SavedInventoryMenu.Action.RESTORE);
    }

    private void sendButtonToServer(SavedInventoryMenu.Action action) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, action.ordinal());
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title.copy().append(" "+menu.dataSlot.get()), this.titleLabelX, this.titleLabelY, 0x404040, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

}
