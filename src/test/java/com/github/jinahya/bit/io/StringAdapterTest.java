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

import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static com.github.jinahya.bit.io.StringAdapter.ascii;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringAdapterTest {

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testAscii(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                   @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final StringAdapter adapter = ascii(31);
        final String expected = new RandomStringGenerator.Builder()
                .withinRange(0, 127)
                .build()
                .generate(current().nextInt(128));
        output.writeValue(adapter, expected);
        final long padded = output.align();
        output.flush();
        final String actual = input.readValue(adapter);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#byteIos"})
    @ParameterizedTest
    void testUtf8(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
                  @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final StringAdapter adapter = new StringAdapter(new BytesAdapter(31, 8), UTF_8);
        final String expected = new RandomStringGenerator.Builder()
                .build()
                .generate(current().nextInt(128));
        output.writeValue(adapter, expected);
        final long padded = output.align();
        output.flush();
        final String actual = input.readValue(adapter);
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }
}
