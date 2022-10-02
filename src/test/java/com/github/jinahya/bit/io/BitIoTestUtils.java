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

import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import static java.util.concurrent.ThreadLocalRandom.current;

final class BitIoTestUtils {

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

    static <R> R w1(
            final Function<? super BitOutput, Function<? super byte[], ? extends R>> f1) throws IOException {
        Objects.requireNonNull(f1, "f1 is null");
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput o = new ByteOutputAdapter(new StreamByteOutput(baos));
        final Function<? super byte[], ? extends R> f2 = f1.apply(o);
        final long padded = o.align();
        assert padded >= 0L;
        final byte[] bytes = baos.toByteArray();
        assert f2 != null : "f2 is null";
        return f2.apply(bytes);
    }

    static <R> R w1v(
            final CheckedFunction1<? super BitOutput, CheckedFunction1<? super byte[], ? extends R>> f1)
            throws IOException {
        Objects.requireNonNull(f1, "f1 is null");
        return w1(o -> {
            final CheckedFunction1<? super byte[], ? extends R> f2 = f1.unchecked().apply(o);
            assert f2 != null : "f2 is null";
            return b -> f2.unchecked().apply(b);
        });
    }

    static <R> R wr1(final Function<? super BitOutput, ? extends Function<? super BitInput, ? extends R>> f1)
            throws IOException {
        Objects.requireNonNull(f1, "f1 is null");
        if (current().nextBoolean()) {
            return w1(o -> {
                final Function<? super BitInput, ? extends R> f2 = f1.apply(o);
                assert f2 != null : "f2 is null";
                return b -> {
                    final ByteArrayInputStream bais = new ByteArrayInputStream(b);
                    final BitInput input = new ByteInputAdapter(new StreamByteInput(bais));
                    final R result = f2.apply(input);
                    try {
                        final long discarded = input.align(1);
                        assert discarded >= 0L;
                        return result;
                    } catch (final IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                };
            });
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput o = new ByteOutputAdapter(new StreamByteOutput(baos));
        final Function<? super BitInput, ? extends R> f2 = f1.apply(o);
        final long padded = o.align();
        final byte[] bytes = baos.toByteArray();
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final BitInput i = new ByteInputAdapter(new StreamByteInput(bais));
        try {
            return f2.apply(i);
        } finally {
            final long discarded = i.align(1);
            assert discarded == padded;
        }
    }

    static <R> R wr2(final Function<? super BitOutput, ? extends Consumer<? super BitInput>> f1) throws IOException {
        Objects.requireNonNull(f1, "f1 is null");
        return wr1(o -> {
            final Consumer<? super BitInput> c1 = f1.apply(o);
            return i -> {
                c1.accept(i);
                return null;
            };
        });
    }

    static <R> R wr1v(
            final CheckedFunction1<? super BitOutput, ? extends CheckedFunction1<? super BitInput, ? extends R>> f1)
            throws IOException {
        Objects.requireNonNull(f1, "f1 is null");
        return wr1(o -> {
            final CheckedFunction1<? super BitInput, ? extends R> f2 = f1.unchecked().apply(o);
            return i -> f2.unchecked().apply(i);
        });
    }

    static <R> R wr2v(final CheckedFunction1<? super BitOutput, ? extends CheckedConsumer<? super BitInput>> f1)
            throws IOException {
        Objects.requireNonNull(f1, "f1 is null");
        return wr1v(o -> {
            final CheckedConsumer<? super BitInput> c1 = f1.unchecked().apply(o);
            return i -> {
                c1.unchecked().accept(i);
                return null;
            };
        });
    }

    private BitIoTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
