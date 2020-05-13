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

import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForChar;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForUnsignedLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForChar;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedLong;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Test parameters for {@link BitInput} and {@link BitOutput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
final class BitIoTestParameters {

    // ------------------------------------------------------------------------------------------------------------ byte
    static IntStream invalidSizesForByte() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Byte.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Byte.SIZE) + 1);
        return builder.build();
    }

    static IntStream invalidSizesForUnsignedByte() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Byte.SIZE);
        builder.add(current().nextInt() >>> 1 | Byte.SIZE);
        return builder.build();
    }

    // ----------------------------------------------------------------------------------------------------------- short
    static IntStream invalidSizesForShort() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Short.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Short.SIZE) + 1);
        return builder.build();
    }

    static IntStream invalidSizesForUnsignedShort() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Short.SIZE);
        builder.add(current().nextInt() >>> 1 | Short.SIZE);
        return builder.build();
    }

    // ------------------------------------------------------------------------------------------------------------- int
    static IntStream illegalSizesForInt() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Integer.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Integer.SIZE) + 1);
        return builder.build();
    }

    static IntStream illegalSizesForUnsignedInt() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Integer.SIZE);
        builder.add(current().nextInt() >>> 1 | Integer.SIZE);
        return builder.build();
    }

    static IntStream sizesForInt() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Integer.SIZE);
        range(0, 128).map(i -> randomSizeForInt()).forEach(builder::add);
        return builder.build();
    }

    static IntStream sizesForUnsignedInt() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Integer.SIZE - 1);
        range(0, 128).map(i -> randomSizeForUnsignedInt()).forEach(builder::add);
        return builder.build();
    }

    static Stream<Arguments> sizesAndValuesForInt() {
        return sizesForInt().mapToObj(size -> arguments(size, randomValueForInt(size)));
    }

    static Stream<Arguments> sizesAndValuesForUnsignedInt() {
        return sizesForUnsignedInt().mapToObj(size -> arguments(size, randomValueForUnsignedInt(size)));
    }

    // ------------------------------------------------------------------------------------------------------------ long
    static IntStream illegalSizesForLong() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Long.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Long.SIZE) + 1);
        return builder.build();
    }

    static IntStream illegalSizesForUnsignedLong() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Long.SIZE);
        builder.add(current().nextInt() >>> 1 | Long.SIZE);
        return builder.build();
    }

    static IntStream sizesForLong() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Long.SIZE);
        range(0, 128).map(i -> randomSizeForLong()).forEach(builder::add);
        return builder.build();
    }

    static IntStream sizesForUnsignedLong() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Long.SIZE - 1);
        range(0, 128).map(i -> randomSizeForUnsignedLong()).forEach(builder::add);
        return builder.build();
    }

    static Stream<Arguments> sizesAndValuesForLong() {
        return sizesForLong().mapToObj(size -> arguments(size, randomValueForLong(size)));
    }

    static Stream<Arguments> sizesAndValuesForUnsignedLong() {
        return sizesForUnsignedLong().mapToObj(size -> arguments(size, randomValueForUnsignedLong(size)));
    }

    // ------------------------------------------------------------------------------------------------------------ char
    static IntStream invalidSizesForChar() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Character.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Character.SIZE) + 1);
        return builder.build();
    }

    static IntStream sizesForChar() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Character.SIZE);
        range(0, 128).map(i -> randomSizeForChar()).forEach(builder::add);
        return builder.build();
    }

    static Stream<Arguments> sizesAndValuesForChar() {
        return sizesForChar().mapToObj(size -> arguments(size, randomValueForChar(size)));
    }

    // ------------------------------------------------------------------------------------------------------------ skip
    static IntStream illegalBitsForSkip() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        return builder.build();
    }

    // ----------------------------------------------------------------------------------------------------------- align
    static IntStream illegalBytesForAlign() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        return builder.build();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoTestParameters() {
        throw new AssertionError("initialization is not allowed");
    }
}
