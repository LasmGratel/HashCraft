package com.projecturanus.hashcraft;

import mezz.jei.Internal;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;

@JEIPlugin
public class HashCraftPlugin implements IModPlugin {
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        var list = Internal.getIngredientRegistry().getAllIngredients(VanillaTypes.ITEM);
        var i = 0;
        var iTime = 0L;
        var j = 0;
        var jTime = 0L;
        for (var stack : list) {
            if (stack.hasTagCompound()) {
                var time = System.nanoTime();
                assert HashCraft.digestItemStack(stack).length == 256;
                iTime += System.nanoTime() - time;
                i++;

                if (i >= 100) {
                    System.out.println("Hash 100 compound itemstacks took " + iTime);
                    i = 0;
                    iTime = 0;
                }
            } else {
                var time = System.nanoTime();
                assert HashCraft.digestItemStack(stack).length == 256;
                jTime += System.nanoTime() - time;
                j++;

                if (j >= 1000) {
                    System.out.println("Hash 1000 normal itemstacks took " + jTime);
                    j = 0;
                    jTime = 0;
                }
            }
        }

        System.out.println("Hash " + i + " compound itemstacks took " + iTime + ", Hash 200 normal itemstacks took " + jTime);
    }
}
