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
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;

final class BitIoTestUtils {

    static <R> R w1(
            final Function<? super BitOutput, Function<? super byte[], ? extends R>> f1) throws IOException {
        Objects.requireNonNull(f1, "f1 is null");
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput o = BitOutputAdapter.of(StreamByteOutput.of(baos));
        final Function<? super byte[], ? extends R> f2 = f1.apply(o);
        final long padded = o.align();
        assert padded >= 0L;
        o.flush();
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
        if (ThreadLocalRandom.current().nextBoolean()) {
            return w1(o -> {
                final Function<? super BitInput, ? extends R> f2 = f1.apply(o);
                assert f2 != null : "f2 is null";
                return b -> {
                    final ByteArrayInputStream bais = new ByteArrayInputStream(b);
                    final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
                    final R result = f2.apply(input);
                    try {
                        final long discarded = input.align();
                        assert discarded >= 0L;
                        return result;
                    } catch (final IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                };
            });
        } else {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final BitOutput o = BitOutputAdapter.of(StreamByteOutput.of(baos));
            final Function<? super BitInput, ? extends R> f2 = f1.apply(o);
            final long padded = o.align();
            final byte[] bytes = baos.toByteArray();
            final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            final BitInput i = BitInputAdapter.of(StreamByteInput.of(bais));
            try {
                return f2.apply(i);
            } finally {
                final long discarded = i.align();
                assert discarded == padded;
            }
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
