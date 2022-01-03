package com.github.jinahya.bit.io;

import java.util.Objects;
import java.util.function.BiFunction;

final class BitIoTestUtils {

    static <R> R writeAndRead(final BiFunction<? super BitInput, ? super BitOutput, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return null;
    }

    private BitIoTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
