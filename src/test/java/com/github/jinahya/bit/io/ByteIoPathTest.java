package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.concurrent.ThreadLocalRandom.current;

class ByteIoPathTest {

    @Test
    void wr__(@TempDir final Path tempDir) throws IOException {
        final Path path = Files.createTempFile(tempDir, null, null);
        ByteIoTestUtilities.wrv(
                () -> ByteOutput.from(path),
                () -> ByteInput.from(path),
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
