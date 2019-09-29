package com.mrcrayfish.blockstatechanger.network.message;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.IProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageBlockStateProperty implements IMessage<MessageBlockStateProperty>
{
    private BlockPos pos;
    private String key;
    private String value;

    public MessageBlockStateProperty() {}

    public MessageBlockStateProperty(BlockPos pos, String key, String value)
    {
        this.pos = pos;
        this.key = key;
        this.value = value;
    }

    @Override
    public void encode(MessageBlockStateProperty message, PacketBuffer buffer)
    {
        buffer.writeBlockPos(message.pos);
        buffer.writeString(message.key, 64);
        buffer.writeString(message.value, 64);
    }

    @Override
    public MessageBlockStateProperty decode(PacketBuffer buffer)
    {
        return new MessageBlockStateProperty(buffer.readBlockPos(), buffer.readString(64), buffer.readString(64));
    }

    @Override
    public void handle(MessageBlockStateProperty message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity entity = supplier.get().getSender();
            if(entity != null)
            {
                ServerWorld world = entity.getServerWorld();
                if(world.isAreaLoaded(message.pos, 0))
                {
                    BlockState state = world.getBlockState(message.pos);
                    IProperty<?> property = state.getProperties().stream().filter(property1 -> property1.getName().equals(message.key)).findFirst().orElse(null);
                    if(property != null)
                    {
                        state = this.parseValue(state, property, message.value);
                        world.setBlockState(message.pos, state, 2 | 16);
                    }
                }
            }
        });
    }

    private <T extends Comparable<T>> BlockState parseValue(BlockState state, IProperty<T> property, String value)
    {
        Optional<T> optional = property.parseValue(value);
        if (optional.isPresent()) {
            state = state.with(property, optional.get());
        }
        return state;
    }
}
