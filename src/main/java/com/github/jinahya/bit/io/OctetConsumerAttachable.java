package com.github.jinahya.bit.io;

import java.util.function.IntPredicate;

public interface OctetConsumerAttachable {

    /**
     * Attaches specified octet consumer to this object.
     *
     * @param octetConsumer the octet consumer.
     * @return {@code true} if this object did not already contain the specified octet consumer.
     */
    default boolean attachOctetConsumer(final IntPredicate octetConsumer) {
        throw new UnsupportedOperationException("unsupported");
    }

    /**
     * Detaches specified octet consumer from this object.
     *
     * @param octetConsumer the octet consumer.
     * @return {@code true} if this object contained the specified octet consumer.
     */
    default boolean detachOctetConsumer(final IntPredicate octetConsumer) {
        throw new UnsupportedOperationException("unsupported");
    }
}
