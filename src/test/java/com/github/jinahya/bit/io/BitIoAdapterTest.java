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

import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForByte;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForChar;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForShort;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForUnsignedLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForUnsignedShort;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForByte;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForChar;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForChar16;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForShort;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedShort;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BitIoAdapterTest {

    // --------------------------------------------------------------------------------------------------------- boolean
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testBoolean(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final boolean expected = current().nextBoolean();
            o.writeBoolean(expected);
            assertEquals(7L, o.align());
            o.flush();
            final boolean actual = i.readBoolean();
            assertEquals(7L, i.align());
            assertEquals(expected, actual);
        }
    }

    // ------------------------------------------------------------------------------------------------------------ byte

    /**
     * Tests {@link BitOutput#writeByte(boolean, int, byte)} method and {@link BitInput#readByte(boolean, int)} method.
     *
     * @param output a bit output.
     * @param input  a bit input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testByte_(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final boolean unsigned = current().nextBoolean();
            final int size = randomSizeForByte(unsigned);
            final byte expected = randomValueForByte(unsigned, size);
            o.writeByte(unsigned, size, expected);
            final long padded = o.align();
            o.flush();
            final byte actual = i.readByte(unsigned, size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testByte(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int size = randomSizeForByte();
            final byte expected = randomValueForByte(size);
            o.writeByte(size, expected);
            final long padded = o.align();
            o.flush();
            final byte actual = i.readByte(size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testByte8(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final byte expected = (byte) (current().nextInt() >> (Integer.SIZE - Byte.SIZE));
            o.writeByte8(expected);
            assertEquals(0L, o.align());
            o.flush();
            final int actual = i.readByte8();
            assertEquals(0L, i.align());
            assertEquals(expected, actual);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testUnsignedByte(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                          @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final boolean unsigned = true;
            final int size = randomSizeForByte(unsigned);
            final byte expected = randomValueForByte(unsigned, size);
            o.writeUnsignedByte(size, expected);
            final long padded = o.align();
            o.flush();
            final int actual = i.readUnsignedByte(size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testShort_(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final boolean unsigned = current().nextBoolean();
            final int size = randomSizeForShort(unsigned);
            final short expected = randomValueForShort(unsigned, size);
            o.writeShort(unsigned, size, expected);
            final long padded = o.align();
            o.flush();
            final short actual = i.readShort(unsigned, size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testShort(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int size = randomSizeForShort();
            final short expected = randomValueForShort(size);
            o.writeShort(size, expected);
            final long padded = o.align();
            o.flush();
            final short actual = i.readShort(size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testShort16(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final short expected = (short) current().nextInt();
            o.writeShort16(expected);
            assertEquals(0L, o.align());
            o.flush();
            final short actual = i.readShort16();
            assertEquals(0L, i.align());
            assertEquals(expected, actual);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testShort16Le(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                       @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final short expected = (short) current().nextInt();
            o.writeShort16Le(expected);
            assertEquals(0L, o.align());
            o.flush();
            final short actual = i.readShort16Le();
            assertEquals(0L, i.align());
            assertEquals(expected, actual);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testUnsignedShort(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                           @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int size = randomSizeForUnsignedShort();
            final short expected = randomValueForUnsignedShort(size);
            o.writeUnsignedShort(size, expected);
            final long padded = o.align();
            o.flush();
            final short actual = i.readUnsignedShort(size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testInt_(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final boolean unsigned = current().nextBoolean();
            final int size = randomSizeForInt(unsigned);
            final int expected = randomValueForInt(unsigned, size);
            o.writeInt(unsigned, size, expected);
            final long padded = o.align();
            o.flush();
            final int actual = i.readInt(unsigned, size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testInt(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                 @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int size = randomSizeForInt();
            final int expected = randomValueForInt(size);
            o.writeInt(size, expected);
            final long padded = o.align();
            o.flush();
            final int actual = i.readInt(size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testInt32(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int expected = current().nextInt();
            o.writeInt32(expected);
            assertEquals(0L, o.align());
            o.flush();
            final int actual = i.readInt32();
            assertEquals(0L, i.align());
            assertEquals(expected, actual);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testInt32Le(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int expected = current().nextInt();
            o.writeInt32Le(expected);
            assertEquals(0L, o.align());
            o.flush();
            final int actual = i.readInt32Le();
            assertEquals(0L, i.align());
            assertEquals(expected, actual);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testUnsignedInt(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                         @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int size = randomSizeForUnsignedInt();
            final int expected = randomValueForUnsignedInt(size);
            o.writeUnsignedInt(size, expected);
            final long padded = o.align();
            o.flush();
            final int actual = i.readUnsignedInt(size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testLong_(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final boolean unsigned = current().nextBoolean();
            final int size = randomSizeForLong(unsigned);
            final long expected = randomValueForLong(unsigned, size);
            o.writeLong(unsigned, size, expected);
            final long padded = o.align();
            o.flush();
            final long actual = i.readLong(unsigned, size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testLong(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int size = randomSizeForLong();
            final long expected = randomValueForLong(size);
            o.writeLong(size, expected);
            final long padded = o.align();
            o.flush();
            final long actual = i.readLong(size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testLong64(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final long expected = current().nextLong();
            o.writeLong64(expected);
            assertEquals(0L, o.align());
            o.flush();
            final long actual = i.readLong64();
            assertEquals(0L, i.align());
            assertEquals(expected, actual);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testLong64Le(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                      @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final long expected = current().nextLong();
            o.writeLong64Le(expected);
            assertEquals(0L, o.align());
            o.flush();
            final long actual = i.readLong64Le();
            assertEquals(0L, i.align());
            assertEquals(expected, actual);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testUnsignedLong(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                          @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int size = randomSizeForUnsignedLong();
            final long expected = randomValueForUnsignedLong(size);
            o.writeUnsignedLong(size, expected);
            final long padded = o.align();
            o.flush();
            final long actual = i.readUnsignedLong(size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testChar(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int size = randomSizeForChar();
            final char expected = randomValueForChar(size);
            o.writeChar(size, expected);
            final long padded = o.align();
            o.flush();
            final char actual = i.readChar(size);
            final long discarded = i.align();
            assertEquals(expected, actual);
            assertEquals(padded, discarded);
        }
    }

    /**
     * Tests both {@link BitOutput#writeChar16(char)} method and {@link BitInput#readChar16()} method.
     *
     * @param output a bit output.
     * @param input  a bit input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testChar16(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final char expected = randomValueForChar16();
            o.writeChar16(expected);
            assertEquals(0L, o.align());
            o.flush();
            final char actual = i.readChar16();
            assertEquals(0L, i.align());
            assertEquals(expected, actual);
        }
    }

    // ----------------------------------------------------------------------------------------------------------- float
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testFloat32(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final float[] expected = new float[] {
                    Float.MAX_VALUE, Float.MIN_NORMAL, Float.MIN_VALUE, Float.NaN, Float.NEGATIVE_INFINITY,
                    Float.POSITIVE_INFINITY,
                    current().nextFloat()
            };
            for (final float value : expected) {
                o.writeFloat32(value);
            }
            assertEquals(0L, o.align());
            o.flush();
            final float[] actual = new float[expected.length];
            for (int j = 0; j < actual.length; j++) {
                actual[j] = i.readFloat32();
            }
            assertEquals(0L, i.align());
            assertArrayEquals(expected, actual);
        }
    }

    // ---------------------------------------------------------------------------------------------------------- double
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testDouble64(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                      @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final double[] expected = new double[] {
                    Double.MAX_VALUE, Double.MIN_NORMAL, Double.MIN_VALUE, Double.NaN, Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY,
                    current().nextDouble()
            };
            for (final double value : expected) {
                o.writeDouble64(value);
            }
            assertEquals(0L, o.align());
            o.flush();
            final double[] actual = new double[expected.length];
            for (int j = 0; j < actual.length; j++) {
                actual[j] = i.readDouble64();
            }
            assertEquals(0L, i.align());
            assertArrayEquals(expected, actual);
        }
    }

    // ------------------------------------------------------------------------------------------------------------ skip
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testSkip(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int bits = current().nextInt(1, 128);
            {
                o.skip(bits);
            }
            final long padded = o.align();
            o.flush();
            {
                i.skip(bits);
            }
            final long discarded = i.align();
            assertEquals(padded, discarded);
        }
    }

    // ----------------------------------------------------------------------------------------------------------- align

    /**
     * Tests {@link BitOutput#align()} method and {@link BitInput#align()} method.
     *
     * @param output a bit output.
     * @param input  a bit input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void testAlign(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int bytes = current().nextInt(1, 128);
            {
                o.writeBoolean(current().nextBoolean());
            }
            final long padded = o.align(bytes);
            o.flush();
            {
                i.readBoolean();
            }
            final long discarded = i.align(bytes);
            assertEquals(padded, discarded);
        }
    }
}
