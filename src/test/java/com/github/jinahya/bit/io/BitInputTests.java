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

import java.util.stream.IntStream;

import static java.util.concurrent.ThreadLocalRandom.current;

@Slf4j
final class BitInputTests {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns an int stream of illegal {@code size} parameters for reading/writing signed longs.
     *
     * @return an int stream.
     */
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

    static IntStream illegalBitsForSkip() {
        final IntStream.Builder builder = IntStream.builder();
        builder.add(0);
        builder.add(-1);
        builder.add(current().nextInt() | Integer.MIN_VALUE);
        return builder.build();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitInputTests() {
        super();
    }
}
