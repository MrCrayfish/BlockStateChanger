package com.mrcrayfish.blockstatechanger.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.blockstatechanger.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.StringUtils;

/**
 * Author: MrCrayfish
 */
public class PropertyScreen extends Screen
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/blockstate_background.png");

    private final BlockState state;
    private final BlockPos pos;
    private final int padding = 7;
    private final int xSize = 94;
    private final int ySize;

    public PropertyScreen(BlockState state, BlockPos pos)
    {
        super(CommonComponents.EMPTY);
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
            String label = StringUtils.capitalize(property.getName());
            this.addRenderableWidget(new Button(guiLeft, guiTop + i * 20 + i * 4, 80, 20, Component.literal(label), pressable -> {
                Minecraft.getInstance().setScreen(new ValueScreen(this.state, this.pos, property));
            }));
            i++;
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        int guiLeft = (this.width - this.xSize + this.padding) / 2;
        int guiTop = (this.height - this.ySize) / 2;
        int height = this.ySize - this.padding * 2;
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(poseStack, guiLeft - this.padding, guiTop - this.padding, 0, 0, 94, this.padding);
        Screen.blit(poseStack, guiLeft - this.padding, guiTop, 94, height, 0, 9, 94, 1, 256, 256);
        this.blit(poseStack, guiLeft - this.padding, guiTop + height, 0, 9, 94, this.padding);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
