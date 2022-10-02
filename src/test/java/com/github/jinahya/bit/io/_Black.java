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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ThreadLocalRandom;

final class _Black {

    static class _OutputStream
            extends OutputStream {

        @Override
        public void write(final int b) throws IOException {
            // does nothing.
        }
    }

    static class _ReadableByteChannel
            implements ReadableByteChannel {

        @Override
        public int read(final ByteBuffer dst) throws IOException {
            final var written = ThreadLocalRandom.current().nextInt(dst.remaining() + 1);
            dst.position(dst.position() + written);
            return written;
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() throws IOException {
            // does nothing
        }
    }

    private _Black() {
        throw new AssertionError("instantiation is not allowed");
    }
}