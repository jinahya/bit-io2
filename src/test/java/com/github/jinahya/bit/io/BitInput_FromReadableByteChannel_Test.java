package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.channels.ReadableByteChannel;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link BitInput#from(ReadableByteChannel)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutput_FromWritableByteChannel_Test
 */
class BitInput_FromReadableByteChannel_Test {

    @Test
    void __Mock() {
        final var byteChannel = Mockito.mock(ReadableByteChannel.class);
        final var bitInput = BitInput.from(byteChannel);
        assertThat(bitInput)
                .isNotNull();
    }
}
