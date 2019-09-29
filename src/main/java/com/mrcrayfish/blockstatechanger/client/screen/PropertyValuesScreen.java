package com.mrcrayfish.blockstatechanger.client.screen;

import com.mrcrayfish.blockstatechanger.Reference;
import com.mrcrayfish.blockstatechanger.network.PacketHandler;
import com.mrcrayfish.blockstatechanger.network.message.MessageBlockStateProperty;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/**
 * Author: MrCrayfish
 */
public class PropertyValuesScreen extends Screen
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/blockstate_background.png");

    private BlockState state;
    private BlockPos pos;
    private IProperty<?> property;
    private int padding = 7;
    private int xSize = 94;
    private int ySize;

    public PropertyValuesScreen(BlockState state, BlockPos pos, IProperty<?> property)
    {
        super(NarratorChatListener.field_216868_a);
        this.state = state;
        this.pos = pos;
        this.property = property;
        this.ySize = padding * 2 + property.getAllowedValues().size() * 20 + (property.getAllowedValues().size() - 1) * 4;
    }

    @Override
    protected void init()
    {
        int guiLeft = (this.width - this.xSize + this.padding) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        int i = 0;
        for(Object value : property.getAllowedValues())
        {
            this.addButton(new Button(guiLeft, guiTop + i * 20 + i * 4, 80, 20, value.toString(), pressable -> {
                PacketHandler.instance.sendToServer(new MessageBlockStateProperty(this.pos, this.property.getName(), value.toString()));
                Minecraft.getInstance().displayGuiScreen(null);
            }));
            i++;
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        int guiLeft = (this.width - this.xSize + this.padding) / 2;
        int guiTop = (this.height - this.ySize) / 2;
        int height = this.ySize - this.padding * 2;
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        this.blit(guiLeft - this.padding, guiTop - this.padding, 0, 0, 94, this.padding);
        AbstractGui.blit(guiLeft - this.padding, guiTop, 94, height, 0, 9, 94, 1, 256, 256);
        this.blit(guiLeft - this.padding, guiTop + height, 0, 9, 94, this.padding);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
