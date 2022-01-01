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

class ByteArrayWriterUtf8
        extends ByteArrayWriter {

    ByteArrayWriterUtf8(final int lengthSize) {
        super(lengthSize, Byte.SIZE);
    }

    @Override
    void writeElements(final BitOutput output, final byte[] value) throws IOException {
        for (int i = 0; i < value.length; i++) {
            final byte b = value[i];
            if ((b & 0x7F) == b) { // 0xxx_xxxx
                output.writeUnsignedInt(2, 0b00);
                output.writeUnsignedInt(7, b);
                continue;
            }
            if ((b & 0xDF) == b) { // 110x_xxxx
                output.writeUnsignedInt(2, 0b01);
                output.writeUnsignedInt(5, b);
                output.writeUnsignedInt(6, value[++i]);
                continue;
            }
            if ((b & 0xEF) == b) { // 1110_xxxx
                output.writeUnsignedInt(2, 0b10);
                output.writeUnsignedInt(4, b);
                output.writeUnsignedInt(6, value[++i]);
                output.writeUnsignedInt(6, value[++i]);
                continue;
            }
            assert ((b & 0xF7) == b); // 1111_0xxx
            output.writeUnsignedInt(2, 0b11);
            output.writeUnsignedInt(3, b);
            output.writeUnsignedInt(6, value[++i]);
            output.writeUnsignedInt(6, value[++i]);
            output.writeUnsignedInt(6, value[++i]);
        }
    }
}
