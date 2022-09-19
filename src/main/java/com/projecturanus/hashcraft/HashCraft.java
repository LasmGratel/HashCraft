package com.projecturanus.hashcraft;

import com.google.common.io.ByteStreams;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.Logger;
import ove.crypto.digest.Blake2b;

import java.io.DataOutput;
import java.io.IOException;

@Mod(modid = HashCraft.MODID)
@Mod.EventBusSubscriber
public class HashCraft {
    public static final String MODID = "hashcraft";

    public static Logger logger;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public static void loadComplete(FMLLoadCompleteEvent event) {

    }

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        if (event.getFlags().isAdvanced()) {
            event.getToolTip().add(Hex.encodeHexString(digestItemStack(event.getItemStack())));
        }
    }

    public static byte[] digestItemStack(ItemStack stack) {
        var compound = new NBTTagCompound();
        stack.writeToNBT(compound);
        var output = ByteStreams.newDataOutput(32);

        try {
            compound.write(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Blake2b.Digest.newInstance(32).digest(output.toByteArray());
    }

    public static void writeCompound(NBTTagCompound compound, DataOutput output) throws IOException {
        compound.getKeySet().stream().sorted().forEach(key -> {
            NBTBase nbt = compound.getTag(key);
            try {
                writeEntry(key, nbt, output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        output.writeByte(0);
    }

    private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException {
        output.writeByte(data.getId());
        if (data.getId() != 0) {
            output.writeUTF(name);

            if (data instanceof NBTTagCompound) {
                writeCompound((NBTTagCompound) data, output);
            } else {
                data.write(output);
            }
        }

    }
}
