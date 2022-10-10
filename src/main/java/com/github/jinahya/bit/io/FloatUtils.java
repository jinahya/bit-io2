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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;

final class FloatUtils {

    private static final int DELTA_FOR_NOCACHE = Byte.MAX_VALUE + 1;

    static final Map<Integer, Map<Integer, FloatReader>> READER_INSTANCES
            = Collections.synchronizedMap(new WeakHashMap<>());
//    static final Map<Integer, Map<Integer, FloatReader>> READER_INSTANCES = new WeakHashMap<>();

    @Deprecated
    static FloatReader getFloatReader(final int exponentSize, final int significandSize,
                                      final ReferenceQueue<Integer> exponentSizeQueue,
                                      final Consumer<WeakReference<Integer>> exponentSizeReferenceConsumer,
                                      final ReferenceQueue<Integer> significandSizeQueue,
                                      final Consumer<WeakReference<Integer>> significandSizeReferenceConsumer) {
        final Integer exponentSizeReferent = Integer.valueOf(exponentSize + DELTA_FOR_NOCACHE);
        if (exponentSizeQueue != null) {
            exponentSizeReferenceConsumer.accept(new WeakReference<>(exponentSizeReferent, exponentSizeQueue));
        }
        final Integer significandSizeReferent = Integer.valueOf(significandSize + DELTA_FOR_NOCACHE);
        if (significandSizeQueue != null) {
            significandSizeReferenceConsumer.accept(new WeakReference<>(significandSizeReferent, significandSizeQueue));
        }
        return READER_INSTANCES.computeIfAbsent(
                        exponentSizeReferent,
                        esr -> Collections.synchronizedMap(new WeakHashMap<>())
//                        esr -> new WeakHashMap<>()
                )
                .computeIfAbsent(
                        significandSizeReferent,
                        ssr -> new FloatReader(exponentSize, significandSize)
                );
    }

    static FloatReader getFloatReader(final int exponentSize, final int significandSize) {
        return getFloatReader(exponentSize, significandSize, null, null, null, null);
    }

    static final Map<Integer, Map<Integer, FloatWriter>> WRITER_INSTANCES
            = Collections.synchronizedMap(new WeakHashMap<>());
//    static final Map<Integer, Map<Integer, FloatWriter>> WRITER_INSTANCES = new WeakHashMap<>();

    @Deprecated
    static FloatWriter getFloatWriter(final int exponentSize, final int significandSize,
                                      final ReferenceQueue<Integer> exponentSizeQueue,
                                      final Consumer<WeakReference<Integer>> exponentSizeReferenceConsumer,
                                      final ReferenceQueue<Integer> significandSizeQueue,
                                      final Consumer<WeakReference<Integer>> significandSizeReferenceConsumer) {
        final Integer exponentSizeReferent = Integer.valueOf(exponentSize + DELTA_FOR_NOCACHE);
        if (exponentSizeQueue != null) {
            exponentSizeReferenceConsumer.accept(new WeakReference<>(exponentSizeReferent, exponentSizeQueue));
        }
        final Integer significandSizeReferent = Integer.valueOf(significandSize + DELTA_FOR_NOCACHE);
        if (significandSizeQueue != null) {
            significandSizeReferenceConsumer.accept(new WeakReference<>(significandSizeReferent, significandSizeQueue));
        }
        return WRITER_INSTANCES.computeIfAbsent(
                        exponentSizeReferent,
                        esr -> Collections.synchronizedMap(new WeakHashMap<>())
//                        esr -> new WeakHashMap<>()
                )
                .computeIfAbsent(
                        significandSizeReferent,
                        ssr -> new FloatWriter(exponentSize, significandSize)
                );
    }

    static FloatWriter getFloatWriter(final int exponentSize, final int significandSize) {
        return getFloatWriter(exponentSize, significandSize, null, null, null, null);
    }

    private FloatUtils() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
