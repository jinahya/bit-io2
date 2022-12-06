package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.DataInput;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * A class for testing factory methods defined in {@link ByteInputFactory} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteOutputFactoryTest
 */
class ByteInputFactoryTest {

    @Test
    void _NotNull_Stream() {
        final var input = ByteInputFactory.from(mock(InputStream.class));
        assertThat(input).isNotNull();
    }

    @Test
    void _NotNull_DataInput() {
        final var input = ByteInputFactory.from(mock(DataInput.class));
        assertThat(input).isNotNull();
    }

    @Test
    void _NotNull_RandomAccessFile() {
        final var input = ByteInputFactory.from(mock(RandomAccessFile.class));
        assertThat(input).isNotNull();
    }

    @Test
    void _NotNull_ByteBuffer() {
        final var input = ByteInputFactory.from(ByteBuffer.allocate(0));
        assertThat(input).isNotNull();
    }

    @Test
    void _NotNull_ReadableByteChannel() {
        final var input = ByteInputFactory.from(mock(ReadableByteChannel.class));
        assertThat(input).isNotNull();
    }
}
