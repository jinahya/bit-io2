package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

abstract class AbstractByteOutputTest<T extends AbstractByteOutput<?>> {

    protected AbstractByteOutputTest(final Class<T> outputClass) {
        super();
        this.outputClass = Objects.requireNonNull(outputClass, "outputClass is null");
    }

    protected abstract T newBlackInstance();

    @Test
    void write__() throws IOException {
        final int value = ThreadLocalRandom.current().nextInt(256);
        newBlackInstance().write(value);
    }

    protected final Class<T> outputClass;
}
