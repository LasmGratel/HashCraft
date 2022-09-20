package com.projecturanus.hashcraft.integration;

import com.projecturanus.hashcraft.HashCraft;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.codec.binary.Hex;

@Mod(modid = HashCraftIntegration.MODID, dependencies = "required-after: hashcraft")
@Mod.EventBusSubscriber
public class HashCraftIntegration {
    public static final String MODID = "hashcraftintegration";


   /*@SubscribeEvent
    public static void leftClick(PlayerInteractEvent.LeftClickBlock event) {
        var player = event.getEntityPlayer();
        var chunk = player.world.getChunk((int) Math.floor(player.posX) >> 4, (int) Math.floor(player.posZ) >> 4);
        var compound = new NBTTagCompound();
        var time = System.nanoTime();
        ForgeChunkManager.storeChunkNBT(chunk, compound);
        var hex = Hex.encodeHexString(HashCraft.digestItemStack(event.getItemStack()));
        player.sendMessage(new TextComponentString(hex + ", took " + (System.nanoTime() - time) + "ns"));
    }*/

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {

        if (event.getFlags().isAdvanced()) {
            event.getToolTip().add(Hex.encodeHexString(HashCraft.digestItemStack(event.getItemStack())));
        }
    }
}
