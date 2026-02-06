package com.hypixel.hytale.codec.builder;

import com.hypixel.hytale.codec.KeyedCodec;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BuilderCodec<T> {

    public static <T> Builder<T> builder(Class<T> clazz, Supplier<T> supplier) {
        return new Builder<>();
    }

    public static class Builder<T> {
        public <V> Builder<T> append(KeyedCodec<V> codec, BiConsumer<T, V> setter, Function<T, V> getter) {
            return this;
        }

        public Builder<T> add() {
            return this;
        }

        public BuilderCodec<T> build() {
            return new BuilderCodec<>();
        }
    }
}
