package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.DataInput;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing factory method defined in {@link BitInput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutput_FromOutputStream_Test
 */
class BitInput_FromDataInput_Test {

    @Test
    void __Mock() {
        final var dataInput = Mockito.mock(DataInput.class);
        final var bitInput = BitInput.from(dataInput);
        assertThat(bitInput)
                .isNotNull();
    }
}
