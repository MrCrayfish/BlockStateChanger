package com.mrcrayfish.blockstatechanger;

import com.mrcrayfish.blockstatechanger.client.screen.PropertyScreen;
import com.mrcrayfish.blockstatechanger.network.PacketHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Author: MrCrayfish
 */
@Mod(Reference.MOD_ID)
public class BlockStateChanger
{
    public BlockStateChanger()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        PacketHandler.init();
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if(event.getItemStack().getItem() == Items.DEBUG_STICK)
        {
            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.SUCCESS);
            BlockState state = event.getWorld().getBlockState(event.getPos());
            if(state.getProperties().size() > 0)
            {
                Minecraft.getInstance().displayGuiScreen(new PropertyScreen(state, event.getPos()));
            }
        }
    }
}
