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

public class BytesAdapter implements ValueAdapter<byte[]> {

    private static BytesAdapter BYTES_ADAPTER8;

    public static BytesAdapter bytesAdapter8(final int elementSize) {
        if (BYTES_ADAPTER8 == null) {
            BYTES_ADAPTER8 = new BytesAdapter(Byte.SIZE - 1, elementSize);
        }
        return BYTES_ADAPTER8;
    }

    private static BytesAdapter BYTES_ADAPTER16;

    public static BytesAdapter bytesAdapter16(final int elementSize) {
        if (BYTES_ADAPTER16 == null) {
            BYTES_ADAPTER16 = new BytesAdapter(Short.SIZE - 1, elementSize);
        }
        return BYTES_ADAPTER16;
    }

    private static BytesAdapter BYTES_ADAPTER24;

    public static BytesAdapter bytesAdapter24(final int elementSize) {
        if (BYTES_ADAPTER24 == null) {
            BYTES_ADAPTER24 = new BytesAdapter(Byte.SIZE + Short.SIZE - 1, elementSize);
        }
        return BYTES_ADAPTER24;
    }

    private static BytesAdapter BYTES_ADAPTER31;

    public static BytesAdapter bytesAdapter31(final int elementSize) {
        if (BYTES_ADAPTER31 == null) {
            BYTES_ADAPTER31 = new BytesAdapter(Integer.SIZE - 1, elementSize);
        }
        return BYTES_ADAPTER31;
    }

    public static BytesAdapter unsignedBytesAdapter(final int lengthSize, final int elementSize) {
        return new BytesAdapter(lengthSize, requireValidSizeByte(true, elementSize));
    }

    private static BytesAdapter UNSIGNED_BYTES_ADAPTER8;

    public static BytesAdapter unsignedBytesAdapter8(final int elementSize) {
        if (UNSIGNED_BYTES_ADAPTER8 == null) {
            UNSIGNED_BYTES_ADAPTER8 = unsignedBytesAdapter(Byte.SIZE - 1, elementSize);
        }
        return UNSIGNED_BYTES_ADAPTER8;
    }

    private static BytesAdapter UNSIGNED_BYTES_ADAPTER16;

    public static BytesAdapter unsignedBytesAdapter16(final int elementSize) {
        if (UNSIGNED_BYTES_ADAPTER16 == null) {
            UNSIGNED_BYTES_ADAPTER16 = unsignedBytesAdapter(Short.SIZE - 1, elementSize);
        }
        return UNSIGNED_BYTES_ADAPTER16;
    }

    private static BytesAdapter UNSIGNED_BYTES_ADAPTER24;

    public static BytesAdapter unsignedBytesAdapter24(final int elementSize) {
        if (UNSIGNED_BYTES_ADAPTER24 == null) {
            UNSIGNED_BYTES_ADAPTER24 = unsignedBytesAdapter(Byte.SIZE + Short.SIZE - 1, elementSize);
        }
        return UNSIGNED_BYTES_ADAPTER24;
    }

    private static BytesAdapter UNSIGNED_BYTES_ADAPTER31;

    public static BytesAdapter newInstance31Unsigned(final int elementSize) {
        if (UNSIGNED_BYTES_ADAPTER31 == null) {
            UNSIGNED_BYTES_ADAPTER31 = unsignedBytesAdapter(Integer.SIZE - 1, elementSize);
        }
        return UNSIGNED_BYTES_ADAPTER31;
    }

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
