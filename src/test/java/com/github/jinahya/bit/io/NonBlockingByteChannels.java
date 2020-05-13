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
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import static java.util.concurrent.ThreadLocalRandom.current;

final class NonBlockingByteChannels {

    private static class Readable implements ReadableByteChannel {

        @Override
        public int read(final ByteBuffer dst) throws IOException {
            if (!dst.hasRemaining()) {
                return 0;
            }
            final int r = current().nextInt(dst.remaining());
            dst.position(dst.position() + r);
            return r;
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public void close() throws IOException {
            // does nothing.
        }
    }

    static ReadableByteChannel readable() {
        return new Readable();
    }

    private static class Writable implements WritableByteChannel {

        @Override
        public int write(final ByteBuffer src) throws IOException {
            if (!src.hasRemaining()) {
                return 0;
            }
            final int w = current().nextInt(src.remaining());
            src.position(src.position() + w);
            return w;
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public void close() throws IOException {
            // does nothing.
        }
    }

    static WritableByteChannel writable() {
        return new Writable();
    }

    private NonBlockingByteChannels() {
        super();
    }
}
