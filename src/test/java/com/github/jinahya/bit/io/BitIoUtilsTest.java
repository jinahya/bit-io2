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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class BitIoUtilsTest {

    @Nested
    class SizeIntTest {

        @Test
        void size_One_Zero() {
            final int value = 0;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {}", value, size);
            assertThat(size).isEqualTo(1);
        }

        @Test
        void size_Two_NegativeOne() {
            final int value = -1;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {}", value, size);
            assertThat(size).isEqualTo(2);
        }

        @Test
        void size_One_PositiveOne() {
            final int value = 1;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {}", value, size);
            assertThat(size).isEqualTo(1);
        }

        @Test
        void size_SIZE_MinValue() {
            final int value = Integer.MIN_VALUE;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {} (MIN_VALUE)", value, size);
            assertThat(size).isEqualTo(Integer.SIZE);
        }

        @Test
        void size_SIZEm1_MaxValue() {
            final int value = Integer.MAX_VALUE;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {} (MAX_VALUE)", value, size);
            assertThat(size).isEqualTo(Integer.SIZE - 1);
        }

        @Test
        void size_LtSIZE_RandomPositive() {
            final int value = ThreadLocalRandom.current().nextInt() & Integer.MAX_VALUE;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {} (random positive)", value, size);
            assertThat(size).isPositive().isLessThan(Integer.SIZE);
        }

        @Test
        void size_LeSIZE_RandomNegative() {
            final int value = ThreadLocalRandom.current().nextInt() | Integer.MIN_VALUE;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {} (random negative)", value, size);
            assertThat(size).isPositive().isLessThanOrEqualTo(Integer.SIZE);
        }
    }

    @Nested
    class SizeLongTest {

        @Test
        void size_One_Zero() {
            final long value = 0L;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {}", value, size);
            assertThat(size).isEqualTo(1);
        }

        @Test
        void size_Two_NegativeOne() {
            final long value = -1L;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {}", value, size);
            assertThat(size).isEqualTo(2);
        }

        @Test
        void size_One_PositiveOne() {
            final long value = 1L;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {}", value, size);
            assertThat(size).isEqualTo(1);
        }

        @Test
        void size_SIZE_MinValue() {
            final long value = Long.MIN_VALUE;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {} (MIN_VALUE)", value, size);
            assertThat(size).isEqualTo(Long.SIZE);
        }

        @Test
        void size_SIZEm1_MaxValue() {
            final long value = Long.MAX_VALUE;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {} (MAX_VALUE)", value, size);
            assertThat(size).isEqualTo(Long.SIZE - 1);
        }

        @Test
        void size_LtSIZE_RandomPositive() {
            final long value = ThreadLocalRandom.current().nextInt() & Long.MAX_VALUE;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {} (random positive)", value, size);
            assertThat(size).isPositive().isLessThan(Long.SIZE);
        }

        @Test
        void size_LeSIZE_RandomNegative() {
            final long value = ThreadLocalRandom.current().nextLong() | Long.MIN_VALUE;
            final int size = BitIoUtils.size(value);
            log.debug("size for {}: {} (random negative)", value, size);
            assertThat(size).isPositive().isLessThanOrEqualTo(Long.SIZE);
        }
    }
}
