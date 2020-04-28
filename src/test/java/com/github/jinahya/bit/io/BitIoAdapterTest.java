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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTestValues.randomSizeForByte;
import static com.github.jinahya.bit.io.BitIoTestValues.randomSizeForInt;
import static com.github.jinahya.bit.io.BitIoTestValues.randomSizeForLong;
import static com.github.jinahya.bit.io.BitIoTestValues.randomSizeForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoTestValues.randomSizeForUnsignedLong;
import static com.github.jinahya.bit.io.BitIoTestValues.randomValueForByte;
import static com.github.jinahya.bit.io.BitIoTestValues.randomValueForInt;
import static com.github.jinahya.bit.io.BitIoTestValues.randomValueForLong;
import static com.github.jinahya.bit.io.BitIoTestValues.randomValueForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoTestValues.randomValueForUnsignedLong;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BitIoAdapterTest {

    // --------------------------------------------------------------------------------------------------------- boolean
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testBoolean(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean expected = current().nextBoolean();
        output.writeBoolean(expected);
        assertEquals(7L, output.align());
        final boolean actual = input.readBoolean();
        assertEquals(7L, input.align());
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testByte(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForByte();
        final byte expected = randomValueForByte(size);
        output.writeByte(size, expected);
        final long padded = output.align();
        final byte actual = input.readByte(size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testByte8(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final byte expected = (byte) (current().nextInt() >> (Integer.SIZE - Byte.SIZE));
        output.writeByte8(expected);
        assertEquals(0L, output.align());
        final int actual = input.readByte8();
        assertEquals(0L, input.align());
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testUnsignedByte(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                          @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = true;
        final int size = randomSizeForByte(unsigned);
        final byte expected = randomValueForByte(unsigned, size);
        output.writeUnsignedByte(size, expected);
        final long padded = output.align();
        final int actual = input.readUnsignedByte(size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testInt(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                 @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForInt();
        final int expected = randomValueForInt(size);
        output.writeInt(size, expected);
        final long padded = output.align();
        final int actual = input.readInt(size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testInt32(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int expected = current().nextInt();
        output.writeInt32(expected);
        assertEquals(0L, output.align());
        final int actual = input.readInt32();
        assertEquals(0L, input.align());
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testUnsignedInt(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                         @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForUnsignedInt();
        final int expected = randomValueForUnsignedInt(size);
        output.writeUnsignedInt(size, expected);
        final long padded = output.align();
        final int actual = input.readUnsignedInt(size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testLong(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForLong();
        final long expected = randomValueForLong(size);
        output.writeLong(size, expected);
        final long padded = output.align();
        final long actual = input.readLong(size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoSources#sourceByteIo"})
    @ParameterizedTest
    void testUnsignedLong(@ConvertWith(BitOutputConverter.class) final BitOutput output,
                          @ConvertWith(BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForUnsignedLong();
        final long expected = randomValueForUnsignedLong(size);
        output.writeUnsignedLong(size, expected);
        final long padded = output.align();
        final long actual = input.readUnsignedLong(size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }
}
