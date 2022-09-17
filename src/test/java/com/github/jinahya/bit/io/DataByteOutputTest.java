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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.github.jinahya.bit.io.ByteStreams.black;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.mockito.Mockito.mock;

/**
 * A class for testing {@link DataByteOutputTest} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DataByteInputTest
 */
class DataByteOutputTest
        extends ByteOutputAdapterTest<DataByteOutput, DataOutput> {

    /**
     * Creates a new instance.
     */
    DataByteOutputTest() {
        super(DataByteOutput.class, DataOutput.class);
    }

    @Nested
    class FromTest {

        @Test
        void from__() throws IOException {
            final var source = mock(DataOutput.class);
            try (DataByteOutput output = DataByteOutput.from(source)) {
            }
        }
    }

    /**
     * Tests {@link DataOutput#write(int)} method.
     *
     * @throws IOException if an I/O error occurs.
     * @see DataByteInputTest#testRead()
     */
    @Test
    void testWrite() throws IOException {
        final DataByteOutput output = new DataByteOutput(() -> new DataOutputStream(black(-1L)));
        output.write(current().nextInt(0, 256));
    }
}
