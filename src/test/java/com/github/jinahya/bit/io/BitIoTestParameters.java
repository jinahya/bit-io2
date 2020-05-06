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
    static IntStream invalidSizeForByte() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Byte.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Byte.SIZE) + 1);
        return builder.build();
    }

    static IntStream invalidSizeForUnsignedByte() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Byte.SIZE);
        builder.add(current().nextInt() >>> 1 | Byte.SIZE);
        return builder.build();
    }

    // ----------------------------------------------------------------------------------------------------------- short
    static IntStream invalidSizeForShort() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Short.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Short.SIZE) + 1);
        return builder.build();
    }

    static IntStream invalidSizeForUnsignedShort() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Short.SIZE);
        builder.add(current().nextInt() >>> 1 | Short.SIZE);
        return builder.build();
    }

    // ------------------------------------------------------------------------------------------------------------- int
    static IntStream illegalSizeForInt() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Integer.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Integer.SIZE) + 1);
        return builder.build();
    }

    static IntStream illegalSizeForUnsignedInt() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Integer.SIZE);
        builder.add(current().nextInt() >>> 1 | Integer.SIZE);
        return builder.build();
    }

    static IntStream sizeForInt() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Integer.SIZE);
        range(0, 128).map(i -> randomSizeForInt()).forEach(builder::add);
        return builder.build();
    }

    static IntStream sizeForUnsignedInt() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Integer.SIZE - 1);
        range(0, 128).map(i -> randomSizeForUnsignedInt()).forEach(builder::add);
        return builder.build();
    }

    static Stream<Arguments> sizeAndValueForInt() {
        return sizeForInt().mapToObj(size -> arguments(size, randomValueForInt(size)));
    }

    static Stream<Arguments> sizeAndValueForUnsignedInt() {
        return sizeForUnsignedInt().mapToObj(size -> arguments(size, randomValueForUnsignedInt(size)));
    }

    // ------------------------------------------------------------------------------------------------------------ long
    static IntStream illegalSizeForLong() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Long.SIZE + 1);
        builder.add((current().nextInt() >>> 1 | Long.SIZE) + 1);
        return builder.build();
    }

    static IntStream illegalSizeForUnsignedLong() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        builder.add(Long.SIZE);
        builder.add(current().nextInt() >>> 1 | Long.SIZE);
        return builder.build();
    }

    static IntStream sizeForLong() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Long.SIZE);
        range(0, 128).map(i -> randomSizeForLong()).forEach(builder::add);
        return builder.build();
    }

    static IntStream sizeForUnsignedLong() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(1);
        builder.add(Long.SIZE - 1);
        range(0, 128).map(i -> randomSizeForUnsignedLong()).forEach(builder::add);
        return builder.build();
    }

    static Stream<Arguments> sizeAndValueForLong() {
        return sizeForLong().mapToObj(size -> arguments(size, randomValueForLong(size)));
    }

    static Stream<Arguments> sizeAndValueForUnsignedLong() {
        return sizeForUnsignedLong().mapToObj(size -> arguments(size, randomValueForUnsignedLong(size)));
    }

    // ------------------------------------------------------------------------------------------------------------ char
    static IntStream invalidSizedForChar() {
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
        super();
    }
}