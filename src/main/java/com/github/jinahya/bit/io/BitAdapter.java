package com.github.jinahya.bit.io;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.zip.Checksum;

import static java.util.Objects.requireNonNull;

abstract class BitBase {

    void update(final int octet) {
        accept(Checksum.class, v -> v.update(octet));
        accept(MessageDigest.class, v -> v.update((byte) octet));
    }

    @SuppressWarnings({"unchecked"})
    private <T> void accept(final Class<T> clazz, final Consumer<? super T> consumer) {
        attachments().compute(
                requireNonNull(clazz, "clazz is null"),
                (k, v) -> {
                    if (v != null) {
                        v.forEach(o -> consumer.accept((T) o));
                    }
                    return v;
                }
        );
    }

    <T> boolean attach(final Class<T> clazz, final T attachment) {
        return attachments()
                .computeIfAbsent(requireNonNull(clazz, "clazz is null"), k -> new HashSet<>())
                .add(requireNonNull(attachment, "attachment is null"));
    }

    <T> boolean detach(final Class<T> clazz, final T attachment) {
        return attachments()
                .computeIfAbsent(requireNonNull(clazz, "clazz is null"), k -> new HashSet<>())
                .remove(requireNonNull(attachment, "attachment is null"));
    }

    private Map<Class<?>, Set<Object>> attachments() {
        if (attachments == null) {
            attachments = new WeakHashMap<>();
        }
        return attachments;
    }

    private Map<Class<?>, Set<Object>> attachments;
}
