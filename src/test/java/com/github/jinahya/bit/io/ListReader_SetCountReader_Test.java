package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListReader_SetCountReader_Test {

    @Test
    void _NullPointerException_Null() {
        final var reader = new ListReader<>(input -> null);
        assertThatThrownBy(() -> reader.countReader(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void __() {
        final var reader = new ListReader<>(input -> null);
        final var actual = reader.countReader(i -> 0);
        assertThat(actual).isSameAs(reader);
    }
}
