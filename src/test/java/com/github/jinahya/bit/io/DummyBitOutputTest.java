package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith({DummyBitOutputParameterResolver.class})
@Slf4j
class DummyBitOutputTest {

    @Test
    void readBoolean__(final BitOutput output) throws IOException {
        final var value = ThreadLocalRandom.current().nextBoolean();
        output.writeBoolean(value);
        verify(output, times(1)).writeBoolean(value);
        verify(output, times(1)).writeUnsignedInt(1, value ? 0b01 : 0b00);
        verify(output, times(1)).writeInt(true, 1, value ? 0b01 : 0b00);
        verifyNoMoreInteractions(output);
    }
}
