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
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.jinahya.bit.io.FloatConstants.SIZE_MIN_SIGNIFICAND;
import static com.github.jinahya.bit.io.FloatConstants.SIZE_SIGNIFICAND;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class FloatReader_CompressedSubnormal_getCachedInstance_Test {

    @Test
    void __() {
//        final Map<Integer, BitReader<Float>> map = new HashMap<>();
//        for (int significandSize = SIZE_MIN_SIGNIFICAND; significandSize <= SIZE_SIGNIFICAND; significandSize++) {
//            final var cachedInstance = FloatReader.CompressedSubnormal.getCachedInstance(significandSize);
//            assertThat(cachedInstance).isNotNull();
//            assertThat(map.put(significandSize, cachedInstance)).isNull();
//        }
//        for (int significandSize = SIZE_MIN_SIGNIFICAND; significandSize <= SIZE_SIGNIFICAND; significandSize++) {
//            final var cachedInstance = FloatReader.CompressedSubnormal.getCachedInstance(significandSize);
//            assertThat(cachedInstance).isSameAs(map.get(significandSize));
//        }
    }

    @Test
    void __Nullable() {
//        final Map<Integer, BitReader<Float>> map = new HashMap<>();
//        for (int size = SIZE_MIN_SIGNIFICAND; size <= SIZE_SIGNIFICAND; size++) {
//            final var instance = FloatReader.CompressedSubnormal.getCachedInstance(size).nullable();
//            assertThat(instance).isNotNull();
//            assertThat(map.put(size, instance)).isNull();
//        }
//        for (int size = SIZE_MIN_SIGNIFICAND; size <= SIZE_SIGNIFICAND; size++) {
//            final var instance = FloatReader.CompressedSubnormal.getCachedInstance(size).nullable();
//            assertThat(instance).isSameAs(map.get(size));
//        }
    }
}
