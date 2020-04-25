package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 Jinahya, Inc.
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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Utilities for testing.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
final class BitIoTests {

    // -----------------------------------------------------------------------------------------------------------------
    static IntStream illegalSizeForIntSigned() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Integer.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Integer.SIZE) + 1);
        return builder.build();
    }

    static IntStream illegalSizeForIntUnsigned() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Integer.SIZE);
        builder.add(current().nextInt() >>> 1 | Integer.SIZE);
        return builder.build();
    }

    static int randomSizeForIntSigned() {
        return requireValidSizeInt(false, current().nextInt(Integer.SIZE) + 1); // 1 - 32
    }

    static IntStream sizeForIntSigned() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Integer.SIZE);
        range(0, 128).map(i -> randomSizeForIntSigned()).forEach(builder::add);
        return builder.build();
    }

    static int randomValueForIntSigned(final int size) {
        return current().nextInt() >> (Integer.SIZE - requireValidSizeInt(false, size));
    }

    /**
     * Returns a stream of arguments of {@code size} and {@code value} valid for {@link BitOutput#writeInt(boolean, int,
     * int) writeInt(false, , )}.
     *
     * @return a stream of arguments.
     */
    static Stream<Arguments> sizeAndValueForIntSigned() {
        return sizeForIntSigned().mapToObj(size -> arguments(size, randomValueForIntSigned(size)));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static int randomSizeForIntUnsigned() {
        return requireValidSizeInt(true, current().nextInt(1, Integer.SIZE)); // 0 - 31
    }

    static IntStream sizeForIntUnsigned() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Integer.SIZE - 1);
        range(0, 128).map(i -> randomSizeForIntUnsigned()).forEach(builder::add);
        return builder.build();
    }

    static int randomValueForIntUnsigned(final int size) {
        return (current().nextInt() >>> 1) >> (Integer.SIZE - requireValidSizeInt(true, size));
    }

    /**
     * Returns a stream of arguments of {@code size} and {@code value} valid for {@link BitOutput#writeInt(boolean, int,
     * int) writeInt(true, , )}.
     *
     * @return a stream of arguments.
     */
    static Stream<Arguments> sizeAndValueForIntUnsigned() {
        return sizeForIntUnsigned().mapToObj(size -> arguments(size, randomValueForIntUnsigned(size)));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static IntStream illegalSizeForLongSigned() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Long.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Long.SIZE) + 1);
        return builder.build();
    }

    static IntStream illegalSizeForLongUnsigned() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Long.SIZE);
        builder.add(current().nextInt() >>> 1 | Long.SIZE);
        return builder.build();
    }

    static int randomSizeForLongSigned() {
        return requireValidSizeLong(false, current().nextInt(Long.SIZE) + 1); // 1 - 64
    }

    static IntStream sizeForLongSigned() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Long.SIZE);
        range(0, 128).map(i -> randomSizeForLongSigned()).forEach(builder::add);
        return builder.build();
    }

    static int randomSizeForLongUnsigned() {
        return requireValidSizeLong(true, current().nextInt(1, Long.SIZE)); // 1 - 63
    }

    static IntStream sizeForLongUnsigned() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Long.SIZE - 1);
        range(0, 128).map(i -> randomSizeForLongUnsigned()).forEach(builder::add);
        return builder.build();
    }

    static long randomValueForLongSigned(final int size) {
        return current().nextLong() >> (Long.SIZE - requireValidSizeLong(false, size));
    }

    static long randomValueForLongUnsigned(final int size) {
        return (current().nextLong() >>> 1) >> (Long.SIZE - requireValidSizeLong(true, size));
    }

    static Stream<Arguments> sizeAndValueForLongSigned() {
        return sizeForLongSigned().mapToObj(size -> arguments(size, randomValueForLongSigned(size)));
    }

    static Stream<Arguments> sizeAndValueForLongUnsigned() {
        return sizeForLongUnsigned().mapToObj(size -> arguments(size, randomValueForLongUnsigned(size)));
    }

    static int randomSizeForLong(final boolean unsigned) {
        return unsigned ? randomSizeForLongUnsigned() : randomSizeForLongSigned();
    }

    static long randomValueForLong(final boolean unsigned, final int size) {
        return unsigned ? randomValueForLongUnsigned(size) : randomValueForLongSigned(size);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static IntStream illegalBitsForSkip() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        return builder.build();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoTests() {
        super();
    }
}
