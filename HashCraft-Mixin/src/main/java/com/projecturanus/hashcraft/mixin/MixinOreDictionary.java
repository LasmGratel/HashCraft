package com.projecturanus.hashcraft.mixin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@Mixin(value = OreDictionary.class, remap = false)
public class MixinOreDictionary {

    // Inject before copy
    @Inject(method = "registerOreImpl", at = @At(value = "INVOKE_ASSIGN", target = "*()Lnet/minecraft/item/ItemStack;"))
    public static void injectRegisterOreImpl(String name, @Nonnull ItemStack ore, CallbackInfo ci) {

    }
}
