package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class FilterBitWriter_Nullable_Test {

    @Test
    void create__() {
        @SuppressWarnings({"unchecked"})
        final var nullable = new FilterBitWriter.Nullable<Object>(mock(BitWriter.class));
    }

    @Test
    void nullable_Throw_AlreadyNullable() {
        @SuppressWarnings({"unchecked"})
        final var nullable = new FilterBitWriter.Nullable<Object>(mock(BitWriter.class));
        assertThatThrownBy(nullable::nullable)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage(BitIoConstants.MESSAGE_UNSUPPORTED_ALREADY_NULLABLE);
    }
}
