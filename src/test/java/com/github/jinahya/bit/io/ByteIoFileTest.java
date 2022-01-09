package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;

class ByteIoFileTest {

    @Test
    void wr__(@TempDir final File tempDir) throws IOException {
        final File file = File.createTempFile("tmp", null, tempDir);
        ByteIoTestUtilities.wrv(
                () -> StreamByteOutput.from(file),
                () -> StreamByteInput.from(file),
                o -> {
                    final int count = current().nextInt(128);
                    for (int j = 0; j < count; j++) {
                        o.write(current().nextInt(256));
                    }
                    return i -> {
                        for (int k = 0; k < count; k++) {
                            final int b = i.read();
                        }
                        return null;
                    };
                }
        );
    }
}
