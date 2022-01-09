package com.github.jinahya.bit.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;

@ExtendWith({BitOutputAdapterParameterResolver.class})
class BitOutputAdapterTest {

    @DisplayName("readBoolean")
    @Nested
    class ReadBooleanTest {

        @Test
        void writeBoolean__(final BitOutput output) throws IOException {
            final boolean value = current().nextBoolean();
            output.writeBoolean(value);
        }
    }
}
