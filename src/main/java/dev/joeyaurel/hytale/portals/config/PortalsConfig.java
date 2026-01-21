package dev.joeyaurel.hytale.portals.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class PortalsConfig {

    public static final BuilderCodec<PortalsConfig> CODEC = BuilderCodec.builder(PortalsConfig.class, PortalsConfig::new)
            .append(new KeyedCodec<>("Test", Codec.INTEGER),
                    (config, integer, _) -> config.Test = integer,
                    (config, _) -> config.Test).add()
            .build();

    private int Test = 25;

    public int getTest() {
        return Test;
    }
}
