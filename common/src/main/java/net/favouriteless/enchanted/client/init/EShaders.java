package net.favouriteless.enchanted.client.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EShaders {

    public static @Nullable ShaderInstance PARTICLE_NO_CUTOFF;

    public static void load(TriConsumer<String, VertexFormat, Consumer<ShaderInstance>> consumer) {
        consumer.accept("particle", DefaultVertexFormat.PARTICLE, i -> PARTICLE_NO_CUTOFF = i);
    }

}
