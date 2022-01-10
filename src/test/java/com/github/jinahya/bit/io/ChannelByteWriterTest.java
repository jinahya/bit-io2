package com.github.jinahya.bit.io;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
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

class ChannelByteWriterTest {

    @RepeatedTest(16)
    void from__Path(@TempDir final Path tempDir) throws IOException {
        final byte[] expected = new byte[current().nextInt(16)];
        current().nextBytes(expected);
        final Path path = Files.createTempFile(tempDir, null, null);
        try (ByteOutput output = ChannelByteOutput.from(path, allocate(current().nextInt(1, 8)))) {
            for (final byte b : expected) {
                output.write(b & 0XFF);
            }
            output.flush();
        }
        assertThat(Files.size(path)).isEqualTo(expected.length);
    }

    @RepeatedTest(16)
    void from__Channel(@TempDir final Path tempDir) throws IOException {
        final byte[] expected = new byte[current().nextInt(16)];
        current().nextBytes(expected);
        final Path path = Files.createTempFile(tempDir, null, null);
        try (FileChannel channel = spy(FileChannel.open(path, StandardOpenOption.WRITE))) {
            try (ByteOutput output = ChannelByteOutput.from(channel, allocate(current().nextInt(1, 8)))) {
                for (final byte b : expected) {
                    output.write(b & 0XFF);
                }
                output.flush();
                verify(channel, times(1)).force(anyBoolean());
            }
            verify(channel, times(0)).close();
        }
        assertThat(Files.size(path)).isEqualTo(expected.length);
    }
}
