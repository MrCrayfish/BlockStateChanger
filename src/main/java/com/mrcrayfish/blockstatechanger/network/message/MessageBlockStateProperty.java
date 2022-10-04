package com.mrcrayfish.blockstatechanger.network.message;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.network.NetworkEvent;

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
    public void encode(MessageBlockStateProperty message, FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(message.pos);
        buffer.writeUtf(message.key, 64);
        buffer.writeUtf(message.value, 64);
    }

    @Override
    public MessageBlockStateProperty decode(FriendlyByteBuf buffer)
    {
        return new MessageBlockStateProperty(buffer.readBlockPos(), buffer.readUtf(64), buffer.readUtf(64));
    }

    @Override
    public void handle(MessageBlockStateProperty message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayer entity = supplier.get().getSender();
            if(entity != null)
            {
                ServerLevel world = entity.getLevel();
                if(world.isAreaLoaded(message.pos, 0))
                {
                    BlockState state = world.getBlockState(message.pos);
                    Property<?> property = state.getProperties().stream().filter(property1 -> property1.getName().equals(message.key)).findFirst().orElse(null);
                    if(property != null)
                    {
                        state = this.parseValue(state, property, message.value);
                        world.setBlock(message.pos, state, 2 | 16);
                    }
                }
            }
        });
    }

    private <T extends Comparable<T>> BlockState parseValue(BlockState state, Property<T> property, String value)
    {
        Optional<T> optional = property.getValue(value);
        if (optional.isPresent()) {
            state = state.setValue(property, optional.get());
        }
        return state;
    }
}
