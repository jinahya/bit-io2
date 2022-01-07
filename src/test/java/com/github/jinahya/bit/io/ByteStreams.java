package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 Jinahya, Inc.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.LongAdder;

import static java.util.concurrent.ThreadLocalRandom.current;

final class ByteStreams {

    static InputStream white(final long bytes) {
        if (bytes < -1) {
            throw new IllegalArgumentException("bytes(" + bytes + ") < -1");
        }
        final LongAdder adder = new LongAdder();
        return new InputStream() {
            @Override
            public int read() throws IOException {
                if (bytes != -1L && adder.sum() >= bytes) {
                    return -1;
                }
                adder.add(1L);
                return current().nextInt(0, 256);
            }
        };
    }

    static OutputStream black(final long bytes) {
        if (bytes < -1L) {
            throw new IllegalArgumentException("bytes(" + bytes + ") < -1");
        }
        final LongAdder adder = new LongAdder();
        return new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                if (bytes != -1L && adder.sum() >= bytes) {
                    throw new IOException("can't write anymore");
                }
                adder.add(1L);
            }
        };
    }

    private ByteStreams() {
        throw new AssertionError("instantiation is not allowed");
    }
}
