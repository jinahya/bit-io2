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

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import static com.github.jinahya.bit.io.ByteStreams.white;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * A class for testing {@link DataByteInput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DataByteOutputTest
 */
class DataByteInputTest
        extends ByteInputAdapterTest<DataByteInput, DataInput> {

    /**
     * Creates a new instance.
     */
    DataByteInputTest() {
        super(DataByteInput.class, DataInput.class);
    }

    @Nested
    class FromTest {

        @Test
        void from__() throws IOException {
            final var source = mock(DataInput.class);
            DataByteInput input = new DataByteInput(source);
        }
    }

    /**
     * Asserts {@link DataByteInput#read()} method throws an {@link IllegalArgumentException} when reached to an end.
     */
    @Test
    void assertReadThrowsEofExceptionWhenReachedToAnEnd() {
        final DataByteInput input = new DataByteInput(new DataInputStream(white(0L)));
        assertThrows(EOFException.class, input::read);
    }

    /**
     * Asserts {@link DataByteInput#read()} method returns a valid octet.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testRead() throws IOException {
        final DataByteInput input = new DataByteInput(new DataInputStream(white(-1L)));
        final int value = input.read();
        assertTrue(value >= 0 && value < 256);
    }
}
