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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for testing {@link ArrayByteInput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ArrayByteOutput
 */
@Deprecated
class ArrayByteInputTest extends ByteInputAdapterTest<ArrayByteInput, byte[]> {

    /**
     * Creates a new instance.
     *
     * @see ArrayByteOutputTest#ArrayByteOutputTest()
     */
    ArrayByteInputTest() {
        super(ArrayByteInput.class, byte[].class);
    }

    // ----------------------------------------------------------------------------------------------- from(InputStream)

    /**
     * Asserts {@link ArrayByteInput#from(InputStream)} method throws a {@code NullPointerException} when {@code source}
     * is {@code null}.
     *
     * @see ArrayByteOutputTest#testFromByteArrayThrowNullPointerExceptionWhenSourceIsNull()
     */
    @DisplayName("from(InputStream) throws NullPointerException when source is null")
    @Test
    void testFromInputStreamArrayThrowNullPointerExceptionWhenSourceIsNull() {
        assertThrows(NullPointerException.class, () -> ArrayByteInput.from((InputStream) null));
    }

    /**
     * Tests {@link ArrayByteInput#from(InputStream)} method with a non-null {@code source} asserting the result is not
     * {@code null}.
     *
     * @see ArrayByteOutputTest#testFromByteArray()
     */
    @DisplayName("from(InputStream) returns non-null")
    @Test
    void testFromInputStream() {
        final ArrayByteInput instance = ArrayByteInput.from(new ByteArrayInputStream(new byte[0]));
        assertNotNull(instance);
    }
}
