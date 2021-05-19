package com.mrcrayfish.blockstatechanger.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.blockstatechanger.Reference;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

/**
 * Author: MrCrayfish
 */
public class PropertyScreen extends Screen
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/blockstate_background.png");

    private BlockState state;
    private BlockPos pos;
    private int padding = 7;
    private int xSize = 94;
    private int ySize;

    public PropertyScreen(BlockState state, BlockPos pos)
    {
        super(StringTextComponent.EMPTY);
        this.state = state;
        this.pos = pos;
        this.ySize = padding * 2 + state.getProperties().size() * 20 + (state.getProperties().size() - 1) * 4;
    }

    @Override
    protected void init()
    {
        int guiLeft = (this.width - this.xSize + this.padding) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        int i = 0;
        for(Property<?> property : state.getProperties())
        {
            this.addButton(new Button(guiLeft, guiTop + i * 20 + i * 4, 80, 20, new StringTextComponent(property.getName()), pressable -> {
                Minecraft.getInstance().displayGuiScreen(new ValueScreen(this.state, this.pos, property));
            }));
            i++;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        int guiLeft = (this.width - this.xSize + this.padding) / 2;
        int guiTop = (this.height - this.ySize) / 2;
        int height = this.ySize - this.padding * 2;
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        this.blit(matrixStack, guiLeft - this.padding, guiTop - this.padding, 0, 0, 94, this.padding);
        AbstractGui.blit(matrixStack, guiLeft - this.padding, guiTop, 94, height, 0, 9, 94, 1, 256, 256);
        this.blit(matrixStack, guiLeft - this.padding, guiTop + height, 0, 9, 94, this.padding);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
