package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing factory methods defined in {@link BitOutput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInput_FromByteBuffer_Test
 */
class BitOutput_FromByteBuffer_Test {

    @Test
    void __ZeroCapacity() {
        final var byteBuffer = ByteBuffer.allocate(0);
        final var bitOutput = BitOutput.from(byteBuffer);
        assertThat(bitOutput)
                .isNotNull();
    }
}
