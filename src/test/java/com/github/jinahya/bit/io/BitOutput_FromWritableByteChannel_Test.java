package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.channels.WritableByteChannel;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link BitOutput#from(WritableByteChannel)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInput_FromReadableByteChannel_Test
 */
class BitOutput_FromWritableByteChannel_Test {

    @Test
    void __Mock() {
        final var byteChannel = Mockito.mock(WritableByteChannel.class);
        final var bitOutput = BitOutput.from(byteChannel);
        assertThat(bitOutput)
                .isNotNull();
    }
}
