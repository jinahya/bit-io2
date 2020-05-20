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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomSizeForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForLong;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoRandomValues.randomValueForUnsignedLong;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Slf4j
class LongArrayTest {

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testSigned(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                    @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int lengthSize = current().nextInt(1, 16);
            final long[] expected = new long[randomValueForUnsignedInt(lengthSize)];
            final int elementSize = randomSizeForLong();
            for (int j = 0; j < expected.length; j++) {
                expected[j] = randomValueForLong(elementSize);
            }
            new LongArrayWriter(lengthSize, elementSize).write(o, expected);
            o.align();
            o.flush();
            final long[] actual = new LongArrayReader(lengthSize, elementSize).read(i);
            i.align();
            assertArrayEquals(expected, actual);
        }
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testUnsigned(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                      @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        try (BitOutput o = output; BitInput i = input) {
            final int lengthSize = current().nextInt(1, 16);
            final long[] expected = new long[randomValueForUnsignedInt(lengthSize)];
            final int elementSize = randomSizeForUnsignedInt();
            for (int j = 0; j < expected.length; j++) {
                expected[j] = randomValueForUnsignedLong(elementSize);
            }
            new LongArrayWriter.Unsigned(lengthSize, elementSize).write(o, expected);
            o.align();
            o.flush();
            final long[] actual = new LongArrayReader.Unsigned(lengthSize, elementSize).read(i);
            i.align();
            assertArrayEquals(expected, actual);
        }
    }
}
