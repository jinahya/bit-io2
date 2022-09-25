package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing factory method defined in {@link BitInput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutput_FromOutputStream_Test
 */
class BitInput_FromInputStream_Test {

    @Test
    void __NullInputStream() {
        final var bitInput = BitInput.from(InputStream.nullInputStream());
        assertThat(bitInput)
                .isNotNull();
    }
}
