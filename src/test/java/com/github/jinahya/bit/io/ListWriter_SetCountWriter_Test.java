package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListWriter_SetCountWriter_Test {

    @Test
    void _NullPointerException_Null() {
        final var writer = new ListWriter<>((o, v) -> {
        });
        assertThatThrownBy(() -> writer.countWriter(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void __() {
        final var writer = new ListWriter<>((o, v) -> {
        });
        final var actual = writer.countWriter((o, c) -> {
        });
        assertThat(actual).isSameAs(writer);
    }
}
