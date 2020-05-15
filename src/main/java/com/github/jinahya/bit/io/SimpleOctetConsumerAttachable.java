package com.github.jinahya.bit.io;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

import static java.util.Objects.requireNonNull;

class SimpleOctetConsumerAttachable implements OctetConsumerAttachable {

    void consumeOctet(final int octet) {
        octetConsumers().removeIf(v -> !v.test(octet));
    }

    @Override
    public boolean attachOctetConsumer(final IntPredicate octetConsumer) {
        requireNonNull(octetConsumer, "octetConsumer is null");
        return octetConsumers().add(octetConsumer);
    }

    @Override
    public boolean detachOctetConsumer(final IntPredicate octetConsumer) {
        requireNonNull(octetConsumer, "octetConsumer is null");
        return octetConsumers().remove(octetConsumer);
    }

    private Set<IntPredicate> octetConsumers() {
        if (octetConsumers == null) {
            octetConsumers = new HashSet<>();
        }
        return octetConsumers;
    }

    private Set<IntPredicate> octetConsumers;
}
