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
 * A byte output writes byte to an array of bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ArrayByteInput
 */
public class ArrayByteOutput extends ByteOutputAdapter<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------
    public ArrayByteOutput(final Supplier<byte[]> targetSupplier) {
        super(targetSupplier);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final byte[] target, final int value) throws IOException {
        target[index++] = (byte) value;
    }

    @Override
    byte[] target() {
        final byte[] target = super.target();
        if (target.length == 0) {
            throw new RuntimeException("zero-length target supplied");
        }
        return target;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The next index in the {@code target} on which the bytes is written.
     */
    protected int index;
}
