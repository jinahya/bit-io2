package com.github.jinahya.bit.io;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import static java.util.concurrent.ThreadLocalRandom.current;

final class RandomPrimitiveValues {

    static byte nextRandomByte(final boolean unsigned, final int size) {
        BitIoConstraints.requireValidSizeForByte(unsigned, size);
        return (byte) ((current().nextInt() & 0xFF) >> (Byte.SIZE - size));
    }

    static <R> R applyRandomByte(final boolean unsigned, final int size, final IntFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextRandomByte(unsigned, size));
    }

    static short nextRandomShort(final boolean unsigned, final int size) {
        BitIoConstraints.requireValidSizeForShort(unsigned, size);
        return (short) ((current().nextInt() & 0xFFFF) >> (Short.SIZE - size));
    }

    static <R> R applyRandomShort(final boolean unsigned, final int size, final IntFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextRandomShort(unsigned, size));
    }

    static int nextRandomInt(final boolean unsigned, final int size) {
        BitIoConstraints.requireValidSizeForInt(unsigned, size);
        if (unsigned) {
            return (current().nextInt() >>> (Integer.SIZE - size));
        } else {
            return (current().nextInt() >> (Integer.SIZE - size));
        }
    }

    static <R> R applyRandomInt(final boolean unsigned, final int size, final IntFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextRandomInt(unsigned, size));
    }

    static long nextRandomLong(final boolean unsigned, final int size) {
        BitIoConstraints.requireValidSizeForLong(unsigned, size);
        if (unsigned) {
            return (current().nextLong() >>> (Long.SIZE - size));
        } else {
            return (current().nextLong() >> (Long.SIZE - size));
        }
    }

    static <R> R applyRandomLong(final boolean unsigned, final int size, final LongFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextRandomLong(unsigned, size));
    }

    private RandomPrimitiveValues() {
        throw new AssertionError("instantiation is not allowed");
    }
}
