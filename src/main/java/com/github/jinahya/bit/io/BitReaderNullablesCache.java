package com.github.jinahya.bit.io;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

final class BitReaderNullablesCache {

    private static final Map<BitReader<?>, BitReader<?>> MAP = new ConcurrentHashMap<>();

    private static final ReferenceQueue<BitReader<?>> QUEUE = new ReferenceQueue<>();

    private static volatile Thread thread = null;

    @SuppressWarnings({"unchecked"})
    static <T extends BitReader<?>> T get(final T reader) {
        Objects.requireNonNull(reader, "reader is null");
        synchronized (BitReaderNullablesCache.class) {
            if (thread == null) {
                thread = new Thread(() -> {
                    while (true) {
                        try {
                            final Reference<? extends BitReader<?>> reference = QUEUE.remove();
                            final BitReader<?> key = reference.get(); // can be null?
                            assert key != null;
                            final BitReader<?> value = MAP.remove(key);
                        } catch (final InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        }
        return (T) MAP.computeIfAbsent(reader, k -> {
            final boolean enqueued = new WeakReference<>(k, QUEUE).enqueue();
            assert enqueued;
            return k.nullable();
        });
    }

    private BitReaderNullablesCache() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
