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

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

final class ByteIoUtils {

    static void clearFillAndFlip(final ByteBuffer buffer, final ReadableByteChannel channel) throws IOException {
        assert !buffer.hasRemaining();
        ((java.nio.Buffer) buffer).clear();
        while (true) {
            final int read = channel.read(buffer);
            if (read > 0) {
                break;
            }
            if (read == -1) {
                throw new EOFException("the channel reached to end-of-stream");
            }
        }
        ((java.nio.Buffer) buffer).flip();
    }

    static void flipFlushAndClear(final ByteBuffer buffer, final WritableByteChannel channel) throws IOException {
        for (((java.nio.Buffer) buffer).flip(); buffer.hasRemaining(); ) {
            channel.write(buffer);
        }
        ((java.nio.Buffer) buffer).clear();
    }

    private ByteIoUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
