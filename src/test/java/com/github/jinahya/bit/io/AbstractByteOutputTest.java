package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

abstract class AbstractByteOutputTest<T extends AbstractByteOutput<U>, U> {

    protected AbstractByteOutputTest(final Class<T> outputClass, final Class<U> targetClass) {
        super();
        this.outputClass = Objects.requireNonNull(outputClass, "outputClass is null");
        this.targetClass = Objects.requireNonNull(targetClass, "targetClass is null");
    }

    protected U newTarget() {
        return Mockito.mock(targetClass);
    }

    protected T newOutput() {
        try {
            return outputClass.getConstructor(targetClass).newInstance(newTarget());
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    @Test
    void write__() throws IOException {
        final int value = ThreadLocalRandom.current().nextInt(256);
        newOutput().write(value);
    }

    protected final Class<T> outputClass;

    protected final Class<U> targetClass;
}
