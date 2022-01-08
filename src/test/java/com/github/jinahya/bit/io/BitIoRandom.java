package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.vavr.CheckedFunction1;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import static java.util.concurrent.ThreadLocalRandom.current;

final class BitIoRandom {

    // ------------------------------------------------------------------------------------------------------------ byte
    static int nextSizeForByte(final boolean unsigned) {
        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
        return BitIoConstraints.requireValidSizeForByte(unsigned, size);
    }

    static <R> R applyNextSizeForByte(final boolean unsigned, final IntFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextSizeForByte(unsigned));
    }

    static byte nextValueForByte(final boolean unsigned, final int size) {
        BitIoConstraints.requireValidSizeForByte(unsigned, size);
        final byte value;
        if (unsigned) {
            value = (byte) (current().nextInt() >>> (Integer.SIZE - size));
        } else {
            value = (byte) (current().nextInt() >> (Integer.SIZE - size));
        }
        return value;
    }

    static <R> R applyNextValueForByte(final boolean unsigned, final int size,
                                       final IntFunction<? extends R> function) {
        BitIoConstraints.requireValidSizeForByte(unsigned, size);
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextValueForByte(unsigned, size));
    }

    static <R> R applyNextValueForByte_v(final boolean unsigned, final int size,
                                         final CheckedFunction1<? super Integer, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return applyNextValueForByte(unsigned, size, v -> function.unchecked().apply(v));
    }

    static <R> R applyNextByte(final boolean unsigned,
                               final IntFunction<? extends IntFunction<? extends R>> function) {
        Objects.requireNonNull(function, "function is null");
        return applyNextSizeForByte(unsigned, s -> applyNextValueForByte(unsigned, s, v -> function.apply(s).apply(v)));
    }

    static <R> R applyNextByte_v(
            final boolean unsigned,
            final CheckedFunction1<? super Integer, ? extends CheckedFunction1<? super Integer, ? extends R>> function) {
        return applyNextByte(unsigned, s -> v -> function.unchecked().apply(s).unchecked().apply(v));
    }

    // ----------------------------------------------------------------------------------------------------------- short
    static int nextSizeForShort(final boolean unsigned) {
        final int size = current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1));
        return BitIoConstraints.requireValidSizeForShort(unsigned, size);
    }

    static <R> R applyNextSizeForShort(final boolean unsigned, final IntFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextSizeForShort(unsigned));
    }

    static short nextValueForShort(final boolean unsigned, final int size) {
        BitIoConstraints.requireValidSizeForShort(unsigned, size);
        final short value;
        if (unsigned) {
            value = (short) (current().nextInt() >>> (Integer.SIZE - size));
        } else {
            value = (short) (current().nextInt() >> (Integer.SIZE - size));
        }
        return value;
    }

    static <R> R applyNextValueForShort(final boolean unsigned, final int size,
                                        final IntFunction<? extends R> function) {
        BitIoConstraints.requireValidSizeForShort(unsigned, size);
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextValueForShort(unsigned, size));
    }

    static <R> R applyNextValueForShort_v(final boolean unsigned, final int size,
                                          final CheckedFunction1<? super Integer, ? extends R> function) {
        return applyNextValueForShort(unsigned, size, v -> function.unchecked().apply(v));
    }

    static <R> R applyNextShort(final boolean unsigned,
                                final IntFunction<? extends IntFunction<? extends R>> function) {
        Objects.requireNonNull(function, "function is null");
        return applyNextSizeForShort(
                unsigned, s -> applyNextValueForShort(unsigned, s, v -> function.apply(s).apply(v)));
    }

    static <R> R applyNextShort_v(
            final boolean unsigned,
            final CheckedFunction1<? super Integer, ? extends CheckedFunction1<? super Integer, ? extends R>> function) {
        return applyNextShort(unsigned, s -> v -> function.unchecked().apply(s).unchecked().apply(v));
    }

    // ------------------------------------------------------------------------------------------------------------- int
    static int nextSizeForInt(final boolean unsigned) {
        return current().nextInt(1, Integer.SIZE + (unsigned ? 0 : 1));
    }

    static <R> R applyNextSizeForInt(final boolean unsigned, final IntFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextSizeForInt(unsigned));
    }

    static int nextValueForInt(final boolean unsigned, final int size) {
        BitIoConstraints.requireValidSizeForInt(unsigned, size);
        if (unsigned) {
            return (current().nextInt() >>> (Integer.SIZE - size));
        } else {
            return (current().nextInt() >> (Integer.SIZE - size));
        }
    }

    static <R> R applyNextValueForInt(final boolean unsigned, final int size, final IntFunction<? extends R> function) {
        BitIoConstraints.requireValidSizeForInt(unsigned, size);
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextValueForInt(unsigned, size));
    }

    static <R> R applyNextInt(final boolean unsigned, final IntFunction<? extends IntFunction<? extends R>> function) {
        Objects.requireNonNull(function, "function is null");
        return applyNextSizeForInt(unsigned, s -> applyNextValueForInt(unsigned, s, v -> function.apply(s).apply(v)));
    }

    static <R> R applyNextInt_v(
            final boolean unsigned,
            final CheckedFunction1<? super Integer, ? extends CheckedFunction1<? super Integer, ? extends R>> function) {
        return applyNextInt(unsigned, s -> v -> function.unchecked().apply(s).unchecked().apply(v));
    }

    // ------------------------------------------------------------------------------------------------------------ long
    static int nextSizeForLong(final boolean unsigned) {
        return current().nextInt(1, Long.SIZE + (unsigned ? 0 : 1));
    }

    static <R> R applyNextSizeForLong(final boolean unsigned, final IntFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextSizeForLong(unsigned));
    }

    static long nextValueForLong(final boolean unsigned, final int size) {
        BitIoConstraints.requireValidSizeForLong(unsigned, size);
        if (unsigned) {
            return (current().nextLong() >>> (Long.SIZE - size));
        } else {
            return (current().nextLong() >> (Long.SIZE - size));
        }
    }

    static <R> R applyNextValueForLong(final boolean unsigned, final int size,
                                       final LongFunction<? extends R> function) {
        BitIoConstraints.requireValidSizeForLong(unsigned, size);
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextValueForLong(unsigned, size));
    }

    static <R> R applyNextLong(final boolean unsigned,
                               final IntFunction<? extends LongFunction<? extends R>> function) {
        Objects.requireNonNull(function, "function is null");
        return applyNextSizeForLong(unsigned, s -> applyNextValueForLong(unsigned, s, v -> function.apply(s).apply(v)));
    }

    static <R> R applyNextLong_v(
            final boolean unsigned,
            final CheckedFunction1<? super Integer, ? extends CheckedFunction1<Long, ? extends R>> function) {
        return applyNextLong(unsigned, s -> v -> function.unchecked().apply(s).unchecked().apply(v));
    }

    // ------------------------------------------------------------------------------------------------------------ char
    static int nextSizeForChar() {
        final int size = current().nextInt(1, Character.SIZE + 1);
        return BitIoConstraints.requireValidSizeForChar(size);
    }

    static <R> R applyNextSizeForChar(final IntFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextSizeForChar());
    }

    static char nextValueForChar(final int size) {
        BitIoConstraints.requireValidSizeForChar(size);
        final char value = (char) (current().nextInt() >>> (Integer.SIZE - size));
        assert value >> size == 0;
        return value;
    }

    static <R> R applyNextValueForChar(final int size, final IntFunction<? extends R> function) {
        BitIoConstraints.requireValidSizeForChar(size);
        Objects.requireNonNull(function, "function is null");
        return function.apply(nextValueForChar(size));
    }

    static <R> R applyNextValueForChar_v(final int size,
                                         final CheckedFunction1<? super Integer, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return applyNextValueForChar(size, v -> function.unchecked().apply(v));
    }

    static <R> R applyNextChar(final IntFunction<? extends IntFunction<? extends R>> function) {
        Objects.requireNonNull(function, "function is null");
        return applyNextSizeForChar(s -> applyNextValueForChar(s, v -> function.apply(s).apply(v)));
    }

    static <R> R applyNextChar_v(
            final CheckedFunction1<? super Integer, ? extends CheckedFunction1<? super Integer, ? extends R>> function) {
        return applyNextChar(s -> v -> function.unchecked().apply(s).unchecked().apply(v));
    }

    private BitIoRandom() {
        throw new AssertionError("instantiation is not allowed");
    }
}
