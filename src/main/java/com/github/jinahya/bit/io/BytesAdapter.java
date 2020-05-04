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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;

/**
 * A value adapter for reading/writing an array of bytes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class BytesAdapter implements ValueAdapter<byte[]> {

    private static class UnsignedBytesAdapter extends BytesAdapter {

        private UnsignedBytesAdapter(final int lengthSize, final int elementSize) {
            super(lengthSize, requireValidSizeByte(true, elementSize));
        }

        @Override
        public void write(final BitOutput output, final byte[] value) throws IOException {
            final int length = value.length & ((1 << lengthSize) - 1);
            output.writeUnsignedInt(lengthSize, length);
            for (int i = 0; i < length; i++) {
                output.writeByte(true, elementSize, value[i]);
            }
        }

        @Override
        public byte[] read(final BitInput input) throws IOException {
            final int length = input.readUnsignedInt(lengthSize);
            final byte[] value = new byte[length];
            for (int i = 0; i < value.length; i++) {
                value[i] = input.readByte(true, elementSize);
            }
            return value;
        }
    }

    public static BytesAdapter unsignedBytesAdapter(final int lengthSize, final int elementSize) {
        return new UnsignedBytesAdapter(lengthSize, elementSize);
    }

    private static BytesAdapter UNSIGNED_BYTES_ADAPTER8;

    /**
     * Creates a new instance with specified arguments.
     *
     * @param lengthSize  a number of bits for the {@code length} of the array.
     * @param elementSize a number of bits for each elements in the array.
     */
    public BytesAdapter(final int lengthSize, final int elementSize) {
        super();
        this.lengthSize = requireValidSizeInt(true, lengthSize);
        this.elementSize = requireValidSizeByte(false, elementSize);
    }

    @Override
    public void write(final BitOutput output, final byte[] value) throws IOException {
        final int length = value.length & ((1 << lengthSize) - 1);
        output.writeUnsignedInt(lengthSize, length);
        for (int i = 0; i < length; i++) {
            output.writeByte(elementSize, value[i]);
        }
    }

    @Override
    public byte[] read(final BitInput input) throws IOException {
        final int length = input.readUnsignedInt(lengthSize);
        final byte[] value = new byte[length];
        for (int i = 0; i < value.length; i++) {
            value[i] = input.readByte(elementSize);
        }
        return value;
    }

    final int lengthSize;

    final int elementSize;
}
