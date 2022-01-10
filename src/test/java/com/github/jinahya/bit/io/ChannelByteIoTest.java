package com.github.jinahya.bit.io;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.ByteBuffer.allocate;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ChannelByteIoTest {

    @RepeatedTest(16)
    void wr__FromPath(@TempDir final Path tempDir) throws IOException {
        final byte[] expected = new byte[current().nextInt(16)];
        current().nextBytes(expected);
        final Path path = Files.createTempFile(tempDir, null, null);
        try (ByteOutput output = ChannelByteOutput.from(path, allocate(current().nextInt(1, 8)))) {
            for (final byte b : expected) {
                output.write(b & 0XFF);
            }
            output.flush();
        }
        final byte[] actual = new byte[expected.length];
        try (ByteInput input = ChannelByteInput.from(
                path, (ByteBuffer) allocate(current().nextInt(1, 8)).flip())) {
            for (int i = 0; i < actual.length; i++) {
                actual[i] = (byte) input.read();
            }
        }
        assertThat(actual).isEqualTo(expected);
    }

    @RepeatedTest(16)
    void wr__FromChannel(@TempDir final Path tempDir) throws IOException {
        final byte[] expected = new byte[current().nextInt(16)];
        current().nextBytes(expected);
        final Path path = Files.createTempFile(tempDir, null, null);
        try (FileChannel wc = spy(FileChannel.open(path, StandardOpenOption.WRITE))) {
            try (ByteOutput output = ChannelByteOutput.from(wc, allocate(current().nextInt(1, 8)))) {
                for (final byte b : expected) {
                    output.write(b & 0XFF);
                }
                output.flush();
                verify(wc, times(1)).force(anyBoolean());
            }
            verify(wc, times(0)).close();
        }
        final byte[] actual = new byte[expected.length];
        try (FileChannel rc = spy(FileChannel.open(path, StandardOpenOption.READ))) {
            try (ByteInput input = ChannelByteInput.from(rc, (ByteBuffer) allocate(current().nextInt(1, 8)).flip())) {
                for (int i = 0; i < actual.length; i++) {
                    actual[i] = (byte) input.read();
                }
            }
            verify(rc, times(0)).close();
        }
        assertThat(actual).isEqualTo(expected);
    }
}
