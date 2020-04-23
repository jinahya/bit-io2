package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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
import java.util.function.Supplier;

/**
 * A byte input reads bytes from an array of bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ArrayByteOutput
 */
public class ArrayByteInput extends ByteInputAdapter<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------
    public ArrayByteInput(final Supplier<byte[]> supplier) {
        super(supplier);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read(final byte[] source) throws IOException {
        return source[index++] & 0xFF;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    byte[] source() {
        final byte[] source = super.source();
        if (source.length == 0) {
            throw new RuntimeException("zero-length source supplied");
        }
        return source;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The next index in the {@code source} on which the byte is read.
     */
    protected int index;
}
