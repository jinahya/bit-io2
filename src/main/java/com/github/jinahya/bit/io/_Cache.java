package com.github.jinahya.bit.io;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

// https://stackoverflow.com/a/16991188/330457
final class _Cache<K, V> {

    _Cache(final Function<? super K, ? extends V> valueMapper) {
        super();
        this.valueMapper = Objects.requireNonNull(valueMapper, "valueMapper is null");
    }

    private Reference<V> reference(final V referent) {
        return new SoftReference<>(referent, valueReferenceQueue);
//        return new WeakReference<>(referent, valueReferenceQueue);
    }

    static int c = 0;

    V get(final K key) {
        Objects.requireNonNull(key, "key is null");
        synchronized (this) {
            if (thread == null) {
                thread = new Thread(() -> {
                    while (true) {
                        try {
                            final Reference<? extends V> dequeuedValueReference = valueReferenceQueue.remove();
//                            System.out.println(++c + " dequeued: " + dequeuedValueReference);
                            synchronized (valueReferences) {
                                valueReferences.remove(keys.remove(dequeuedValueReference));
                            }
                        } catch (final InterruptedException e) {
                            System.err.printf("interrupted while removing from %1$s\n", valueReferenceQueue);
                            Thread.currentThread().interrupt();
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        }
        @SuppressWarnings({"unchecked"})
        final V[] referentHolder = (V[]) new Object[1];
        synchronized (valueReferences) {
            valueReferences
                    .compute(key, (k, v) -> {
                        if (v == null || ((referentHolder[0] = v.get()) == null)) {
                            referentHolder[0] = valueMapper.apply(k);
                            v = reference(referentHolder[0]);
//                            v.enqueue();
                            keys.put(v, k);
                            return v;
                        }
                        return v;
                    });
        }
        return referentHolder[0];
    }

    private final Function<? super K, ? extends V> valueMapper;

    final Map<K, Reference<V>> valueReferences = new HashMap<>();

    private final Map<Reference<V>, K> keys = new HashMap<>();

    private final ReferenceQueue<V> valueReferenceQueue = new ReferenceQueue<>();

    private volatile Thread thread;
}
