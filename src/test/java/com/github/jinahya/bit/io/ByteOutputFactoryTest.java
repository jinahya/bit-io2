package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.DataOutput;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * A class for testing factory methods defines in {@link ByteOutputFactory} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteInputFactoryTest
 */
class ByteOutputFactoryTest {

    @Test
    void _NotNull_OutputStream() {
        final var output = ByteOutputFactory.from(mock(OutputStream.class));
        assertThat(output).isNotNull();
    }

    @Test
    void _NotNull_DataOutput() {
        final var output = ByteOutputFactory.from(mock(DataOutput.class));
        assertThat(output).isNotNull();
    }

    @Test
    void _NotNull_RandomAccessFile() {
        final var output = ByteOutputFactory.from(mock(RandomAccessFile.class));
        assertThat(output).isNotNull();
    }

    @Test
    void _NotNull_ByteBuffer() {
        final var output = ByteOutputFactory.from(ByteBuffer.allocate(0));
        assertThat(output).isNotNull();
    }

    @Test
    void _NotNull_WritableByteChannel() {
        final var output = ByteOutputFactory.from(mock(WritableByteChannel.class));
        assertThat(output).isNotNull();
    }
}
