package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
