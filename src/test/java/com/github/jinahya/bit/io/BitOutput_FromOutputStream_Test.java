package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.OutputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing factory methods defined in {@link BitOutput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInput_FromInputStream_Test
 */
class BitOutput_FromOutputStream_Test {

    @Test
    void __NullOutputStream() {
        final var outputStream = OutputStream.nullOutputStream();
        final var bitOutput = BitOutput.from(outputStream);
        assertThat(bitOutput)
                .isNotNull();
    }
}
