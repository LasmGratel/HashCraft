package com.projecturanus.hashcraft.benchmark;

import com.projecturanus.hashcraft.HashCraft;
import mezz.jei.Internal;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Defaults;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@JEIPlugin
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class HashCraftPlugin implements IModPlugin {
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        Options opt = new OptionsBuilder()
            // set the class name regex for benchmarks to search for to the current class
            .include("\\." + this.getClass().getSimpleName() + "\\.")
            .warmupIterations(Defaults.WARMUP_ITERATIONS)
            .measurementIterations(Defaults.MEASUREMENT_ITERATIONS)
            // do not use forking or the benchmark methods will not see references stored within its class
            .forks(0)
            // do not use multiple threads
            .threads(1)
            .shouldDoGC(true)
            .shouldFailOnError(true)
            .resultFormat(ResultFormatType.JSON)
            .result("report") // set this to a valid filename if you want reports
            .shouldFailOnError(true)
            .build();

        try {
            new Runner(opt).run();
        } catch (RunnerException e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<ItemStack> stacks;

    @Setup
    public void setup() {
        URLClassLoader sysloader = (URLClassLoader) getClass().getClassLoader();
        Class<URLClassLoader> sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(sysloader, new File("C:\\Users\\nanam\\AppData\\Roaming\\MultiMC\\instances\\1.12.2\\.minecraft\\mods\\hashcraft-1.12.2-1.0.0-all.jar").toURI().toURL());
        } catch (Throwable t) {
            t.printStackTrace();
        }//end try catch

        stacks = Internal.getIngredientRegistry().getAllIngredients(VanillaTypes.ITEM);
        System.out.println(stacks.size() + " stacks");
    }

    @Benchmark
    public void benchmark() {
        for (var stack : stacks) {
            assert HashCraft.digestItemStack(stack).length == 256;
            /*
            if (stack.hasTagCompound()) {
                var time = System.nanoTime();
                assert HashCraft.digestItemStack(stack).length == 256;
                iTime += System.nanoTime() - time;
                i++;

                if (i >= 1000) {
                    System.out.println("Hash 1000 compound itemstacks took " + iTime);
                    i = 0;
                    iTime = 0;
                }
            } else {
                var time = System.nanoTime();
                assert HashCraft.digestItemStack(stack).length == 256;
                jTime += System.nanoTime() - time;
                j++;

                if (j >= 5000) {
                    System.out.println("Hash 5000 normal itemstacks took " + jTime + ", average: " + jTime / 5000.0);
                    j = 0;
                    jTime = 0;
                }
            }

             */
        }
    }
}
