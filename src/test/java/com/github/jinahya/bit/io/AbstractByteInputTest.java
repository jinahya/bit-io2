package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

abstract class AbstractByteInputTest<T extends AbstractByteInput<?>> {

    protected AbstractByteInputTest(final Class<T> inputClass) {
        super();
        this.inputClass = Objects.requireNonNull(inputClass, "inputClass is null");
    }

    protected abstract T newWhiteInstance();

    @Test
    void read__() throws IOException {
        final int value = newWhiteInstance().read();
        assertThat(value)
                .isNotNegative()
                .isLessThan(255);
    }

    protected final Class<T> inputClass;
}
