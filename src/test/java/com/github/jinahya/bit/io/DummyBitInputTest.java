package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith({DummyBitInputParameterResolver.class})
@Slf4j
class DummyBitInputTest {

    @Test
    void readBoolean__(final BitInput input) throws IOException {
        final var value = input.readBoolean();
        verify(input, times(1)).readBoolean();
        verify(input, times(1)).readUnsignedInt(1);
        verify(input, times(1)).readInt(true, 1);
        verifyNoMoreInteractions(input);
    }

    @Test
    void readByte__(final BitInput input) throws IOException {
        final var size = BitIoRandomValues.randomSizeForByte();
        final var value = input.readByte(size);
        verify(input, times(1)).readByte(size);
        verify(input, times(1)).readByte(false, size);
        verify(input, times(1)).readInt(false, size);
        verifyNoMoreInteractions(input);
    }
}
