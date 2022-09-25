package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.DataOutput;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing factory methods defined in {@link BitOutput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInput_FromDataInput_Test
 */
class BitOutput_FromDataOutput_Test {

    @Test
    void __Mock() {
        final var dataOutput = Mockito.mock(DataOutput.class);
        final var bitOutput = BitOutput.from(dataOutput);
        assertThat(bitOutput)
                .isNotNull();
    }
}
