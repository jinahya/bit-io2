package com.github.jinahya.bit.io;

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
