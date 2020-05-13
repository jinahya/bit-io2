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
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForByte;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForChar;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForShort;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForUnsignedByte;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForUnsignedLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForUnsignedShort;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForByte;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForChar;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForChar16;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForShort;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedByte;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedShort;
import static com.github.jinahya.bit.io.ValueAdapter.nullable;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BitIoAdapterTest {

    // --------------------------------------------------------------------------------------------------------- boolean
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testBoolean(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean expected = current().nextBoolean();
        output.writeBoolean(expected);
        assertEquals(7L, output.align());
        final boolean actual = input.readBoolean();
        assertEquals(7L, input.align());
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------------------ byte

    /**
     * Tests {@link BitOutput#writeByte(boolean, int, byte)} method and {@link BitInput#readByte(boolean, int)} method.
     *
     * @param output a bit output.
     * @param input  a bit input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testByte_(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForByte(unsigned);
        final byte expected = randomValueForByte(unsigned, size);
        output.writeByte(unsigned, size, expected);
        final long padded = output.align();
        final byte actual = input.readByte(unsigned, size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testByte(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
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

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testByte8(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final byte expected = (byte) (current().nextInt() >> (Integer.SIZE - Byte.SIZE));
        output.writeByte8(expected);
        assertEquals(0L, output.align());
        final int actual = input.readByte8();
        assertEquals(0L, input.align());
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testUnsignedByte(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                          @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
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

    // ----------------------------------------------------------------------------------------------------------- short
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testShort_(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForShort(unsigned);
        final short expected = randomValueForShort(unsigned, size);
        output.writeShort(unsigned, size, expected);
        final long padded = output.align();
        final short actual = input.readShort(unsigned, size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testShort(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForShort();
        final short expected = randomValueForShort(size);
        output.writeShort(size, expected);
        final long padded = output.align();
        final short actual = input.readShort(size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testShort16(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final short expected = (short) current().nextInt();
        output.writeShort16(expected);
        assertEquals(0L, output.align());
        final short actual = input.readShort16();
        assertEquals(0L, input.align());
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testShort16Le(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                       @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final short expected = (short) current().nextInt();
        output.writeShort16Le(expected);
        assertEquals(0L, output.align());
        final short actual = input.readShort16Le();
        assertEquals(0L, input.align());
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testUnsignedShort(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                           @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForUnsignedShort();
        final short expected = randomValueForUnsignedShort(size);
        output.writeUnsignedShort(size, expected);
        final long padded = output.align();
        final short actual = input.readUnsignedShort(size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testInt_(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForInt(unsigned);
        final int expected = randomValueForInt(unsigned, size);
        output.writeInt(unsigned, size, expected);
        final long padded = output.align();
        final int actual = input.readInt(unsigned, size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testInt(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                 @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
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

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testInt32(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final int expected = current().nextInt();
        output.writeInt32(expected);
        assertEquals(0L, output.align());
        final int actual = input.readInt32();
        assertEquals(0L, input.align());
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testInt32Le(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final int expected = current().nextInt();
        output.writeInt32Le(expected);
        assertEquals(0L, output.align());
        final int actual = input.readInt32Le();
        assertEquals(0L, input.align());
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testUnsignedInt(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                         @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
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
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testLong_(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = randomSizeForLong(unsigned);
        final long expected = randomValueForLong(unsigned, size);
        output.writeLong(unsigned, size, expected);
        final long padded = output.align();
        final long actual = input.readLong(unsigned, size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testLong(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
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

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testLong64(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final long expected = current().nextLong();
        output.writeLong64(expected);
        assertEquals(0L, output.align());
        final long actual = input.readLong64();
        assertEquals(0L, input.align());
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testLong64Le(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                      @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final long expected = current().nextLong();
        output.writeLong64Le(expected);
        assertEquals(0L, output.align());
        final long actual = input.readLong64Le();
        assertEquals(0L, input.align());
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testUnsignedLong(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                          @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
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

    // ------------------------------------------------------------------------------------------------------------ char
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testChar(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final int size = randomSizeForChar();
        final char expected = randomValueForChar(size);
        output.writeChar(size, expected);
        final long padded = output.align();
        final char actual = input.readChar(size);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    /**
     * Tests both {@link BitOutput#writeChar16(char)} method and {@link BitInput#readChar16()} method.
     *
     * @param output a bit output.
     * @param input  a bit input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testChar16(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final char expected = randomValueForChar16();
        output.writeChar16(expected);
        assertEquals(0L, output.align());
        final char actual = input.readChar16();
        assertEquals(0L, input.align());
        assertEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------------------- float
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testFloat32(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                     @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final float[] expected = new float[] {
                Float.MAX_VALUE, Float.MIN_NORMAL, Float.MIN_VALUE, Float.NaN, Float.NEGATIVE_INFINITY,
                Float.POSITIVE_INFINITY,
                current().nextFloat()
        };
        for (final float value : expected) {
            output.writeFloat32(value);
        }
        assertEquals(0L, output.align());
        final float[] actual = new float[expected.length];
        for (int j = 0; j < actual.length; j++) {
            actual[j] = input.readFloat32();
        }
        assertEquals(0L, input.align());
        assertArrayEquals(expected, actual);
    }

    // ---------------------------------------------------------------------------------------------------------- double
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testDouble64(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                      @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final double[] expected = new double[] {
                Double.MAX_VALUE, Double.MIN_NORMAL, Double.MIN_VALUE, Double.NaN, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                current().nextDouble()
        };
        for (final double value : expected) {
            output.writeDouble64(value);
        }
        assertEquals(0L, output.align());
        final double[] actual = new double[expected.length];
        for (int j = 0; j < actual.length; j++) {
            actual[j] = input.readDouble64();
        }
        assertEquals(0L, input.align());
        assertArrayEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------------------ skip
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testSkip(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final int bits = current().nextInt(1, 128);
        {
            output.skip(bits);
        }
        final long padded = output.align();
        {
            input.skip(bits);
        }
        final long discarded = input.align();
        assertEquals(padded, discarded);
    }

    // ----------------------------------------------------------------------------------------------------------- align

    /**
     * Tests {@link BitOutput#align()} method and {@link BitInput#align()} method.
     *
     * @param output a bit output.
     * @param input  a bit input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testAlign(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final int bytes = current().nextInt(1, 128);
        {
            output.writeBoolean(current().nextBoolean());
        }
        final long padded = output.align(bytes);
        {
            input.readBoolean();
        }
        final long discarded = input.align(bytes);
        assertEquals(padded, discarded);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testValues(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final int BOOLEAN = 0;
        final int BYTE = BOOLEAN + 1;
        final int UNSIGNED_BYTE = BYTE + 1;
        final int SHORT = UNSIGNED_BYTE + 1;
        final int UNSIGNED_SHORT = SHORT + 1;
        final int INT = UNSIGNED_SHORT + 1;
        final int UNSIGNED_INT = INT + 1;
        final int LONG = UNSIGNED_INT + 1;
        final int UNSIGNED_LONG = LONG + 1;
        final int CHAR = UNSIGNED_LONG + 1;
        final int USER = CHAR + 1;
        final int NULLABLE_USER = USER + 1;
        final List<Integer> types = new ArrayList<>();
        final List<Integer> sizes = new ArrayList<>();
        final List<Object> values = new ArrayList<>();
        final ValueAdapter<User> userAdapter = new UserAdapter();
        final ValueAdapter<User> nullableUserAdapter = nullable(userAdapter);
        final int count = current().nextInt(128);
        for (int i = 0; i < count; i++) {
            final int type = current().nextInt(NULLABLE_USER + 1);
            types.add(type);
            switch (type) {
                case BOOLEAN:
                    sizes.add(null);
                {
                    final boolean value = current().nextBoolean();
                    values.add(value);
                    output.writeBoolean(value);
                }
                break;
                case BYTE: {
                    final int size = randomSizeForByte();
                    sizes.add(size);
                    final byte value = randomValueForByte(size);
                    values.add(value);
                    output.writeByte(size, value);
                }
                break;
                case UNSIGNED_BYTE: {
                    final int size = randomSizeForUnsignedByte();
                    sizes.add(size);
                    final byte value = randomValueForUnsignedByte(size);
                    values.add(value);
                    output.writeUnsignedByte(size, value);
                }
                break;
                case SHORT: {
                    final int size = randomSizeForShort();
                    sizes.add(size);
                    final short value = randomValueForShort(size);
                    values.add(value);
                    output.writeShort(size, value);
                }
                break;
                case UNSIGNED_SHORT: {
                    final int size = randomSizeForUnsignedShort();
                    sizes.add(size);
                    final short value = randomValueForUnsignedShort(size);
                    values.add(value);
                    output.writeUnsignedShort(size, value);
                }
                break;
                case INT: {
                    final int size = randomSizeForInt();
                    sizes.add(size);
                    final int value = randomValueForInt(size);
                    values.add(value);
                    output.writeInt(size, value);
                }
                break;
                case UNSIGNED_INT: {
                    final int size = randomSizeForUnsignedInt();
                    sizes.add(size);
                    final int value = randomValueForUnsignedInt(size);
                    values.add(value);
                    output.writeUnsignedInt(size, value);
                }
                break;
                case LONG: {
                    final int size = randomSizeForLong();
                    sizes.add(size);
                    final long value = randomValueForLong(size);
                    values.add(value);
                    output.writeLong(size, value);
                }
                break;
                case UNSIGNED_LONG: {
                    final int size = randomSizeForUnsignedLong();
                    sizes.add(size);
                    final long value = randomValueForUnsignedLong(size);
                    values.add(value);
                    output.writeUnsignedLong(size, value);
                }
                break;
                case CHAR: {
                    final int size = randomSizeForChar();
                    sizes.add(size);
                    final char value = randomValueForChar(size);
                    values.add(value);
                    output.writeChar(size, value);
                }
                break;
                case USER: {
                    sizes.add(null);
                    final User value = User.newRandomInstance();
                    values.add(value);
                    output.writeValue(userAdapter, value);
                }
                break;
                case NULLABLE_USER: {
                    sizes.add(null);
                    final User value = current().nextBoolean() ? null : User.newRandomInstance();
                    values.add(value);
                    output.writeValue(nullableUserAdapter, value);
                }
                break;
                default:
                    throw new AssertionError("not gonna happen");
            }
        }
        final int bytes = current().nextInt(1, 128);
        output.align(bytes);
        for (int i = 0; i < count; i++) {
            final int type = types.get(i);
            final Integer size = sizes.get(i);
            switch (type) {
                case BOOLEAN: {
                    assertEquals((boolean) values.get(i), input.readBoolean());
                }
                break;
                case BYTE: {
                    assertEquals((byte) values.get(i), input.readByte(size));
                }
                break;
                case UNSIGNED_BYTE: {
                    assertEquals((byte) values.get(i), input.readUnsignedByte(size));
                }
                break;
                case SHORT: {
                    assertEquals((short) values.get(i), input.readShort(size));
                }
                break;
                case UNSIGNED_SHORT: {
                    assertEquals((short) values.get(i), input.readUnsignedShort(size));
                }
                break;
                case INT: {
                    assertEquals((int) values.get(i), input.readInt(size));
                }
                break;
                case UNSIGNED_INT: {
                    assertEquals((int) values.get(i), input.readUnsignedInt(size));
                }
                break;
                case LONG: {
                    assertEquals((long) values.get(i), input.readLong(size));
                }
                break;
                case UNSIGNED_LONG: {
                    assertEquals((long) values.get(i), input.readUnsignedLong(size));
                }
                break;
                case CHAR: {
                    assertEquals((char) values.get(i), input.readChar(size));
                }
                break;
                case USER: {
                    assertEquals(values.get(i), input.readValue(userAdapter));
                }
                break;
                case NULLABLE_USER: {
                    assertEquals(values.get(i), input.readValue(nullableUserAdapter));
                }
                break;
                default:
                    throw new AssertionError("not gonna happen");
            }
        }
        input.align(bytes);
    }
}
