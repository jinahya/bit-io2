package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing factory method defined in {@link BitInput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutput_FromByteBuffer_Test
 */
class BitInput_FromByteBuffer_Test {

    @Test
    void __ZeroCapacity() {
        final var byteBuffer = ByteBuffer.allocate(0);
        final var bitInput = BitInput.from(byteBuffer);
        assertThat(bitInput)
                .isNotNull();
    }
}
