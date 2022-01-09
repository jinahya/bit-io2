package com.github.jinahya.bit.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@ExtendWith({BitInputAdapterParameterResolver.class})
class BitInputAdapterTest {

    @DisplayName("readBoolean")
    @Nested
    class ReadBooleanTest {

        @Test
        void readBoolean__(final BitInput input) throws IOException {
            final boolean value = input.readBoolean();
        }
    }
}
