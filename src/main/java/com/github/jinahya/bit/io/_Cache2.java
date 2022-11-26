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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

// https://stackoverflow.com/a/16991188/330457
final class _Cache2<K, V> {

    _Cache2() {
        super();
    }

    private Reference<V> reference(final V referent) {
        return new SoftReference<>(referent, queue);
    }

    V get(final K key, final Function<? super K, ? extends V> mapper) {
        Objects.requireNonNull(key, "key is null");
        Objects.requireNonNull(mapper, "mapper is null");
        synchronized (this) {
            if (thread == null) {
                thread = new Thread(() -> {
                    while (true) {
                        try {
                            final Reference<? extends V> dequeuedValueReference = queue.remove();
                            synchronized (references) {
                                references.remove(keys.remove(dequeuedValueReference));
                            }
                        } catch (final InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        }
        @SuppressWarnings({"unchecked"})
        final V[] holder = (V[]) new Object[1];
        synchronized (references) {
            references.compute(key, (k, v) -> {
                if (v == null || ((holder[0] = v.get()) == null)) {
                    holder[0] = mapper.apply(k);
                    v = reference(holder[0]);
                    keys.put(v, k);
                    return v;
                }
                return v;
            });
        }
        return holder[0];
    }

    final Map<K, Reference<V>> references = new HashMap<>();

    private final Map<Reference<V>, K> keys = new HashMap<>();

    private final ReferenceQueue<V> queue = new ReferenceQueue<>();

    private Thread thread;
}
