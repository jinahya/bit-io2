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

import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for testing {@link ArrayByteOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ArrayByteInputTest
 */
@Deprecated
class ArrayByteOutputTest extends ByteOutputAdapterTest<ArrayByteOutput, byte[]> {

    /**
     * Creates a new instance.
     *
     * @see ArrayByteInputTest#ArrayByteInputTest()
     */
    ArrayByteOutputTest() {
        super(ArrayByteOutput.class, byte[].class);
    }

    // ---------------------------------------------------------------------------------------------------- from(byte[])

    /**
     * Asserts {@link ArrayByteInput#from(byte[])} method throws a {@code NullPointerException} when {@code source} is
     * {@code null}.
     *
     * @see ArrayByteInputTest#testFromByteArrayThrowNullPointerExceptionWhenSourceIsNull()
     */
    @DisplayName("from(byte[]) throws NullPointerException when source is null")
    @Test
    void testFromByteArrayThrowNullPointerExceptionWhenSourceIsNull() {
        assertThrows(NullPointerException.class, () -> ArrayByteOutput.from((byte[]) null));
    }

    /**
     * Tests {@link ArrayByteInput#from(byte[])} method with a non-null {@code source} asserting the result is not
     * {@code null}.
     *
     * @see ArrayByteInputTest#testFromByteArray()
     */
    @DisplayName("from(byte[]) returns non-null")
    @Test
    void testFromByteArray() {
        final ArrayByteOutput instance = ArrayByteOutput.from(new byte[0]);
        assertNotNull(instance);
    }

    // ---------------------------------------------------------------------------------------------- from(OutputStream)

    /**
     * Asserts {@link ArrayByteOutput#from(OutputStream)} method throws a {@code NullPointerException} when {@code
     * source} is {@code null}.
     *
     * @see ArrayByteOutputTest#testFromByteArrayThrowNullPointerExceptionWhenSourceIsNull()
     */
    @DisplayName("from(OutputStream) throws NullPointerException when source is null")
    @Test
    void testFromOutputStreamArrayThrowNullPointerExceptionWhenSourceIsNull() {
        assertThrows(NullPointerException.class, () -> ArrayByteOutput.from((OutputStream) null));
    }

    /**
     * Tests {@link ArrayByteOutput#from(OutputStream)} method with a non-null {@code source} asserting the result is
     * not {@code null}.
     *
     * @see ArrayByteOutputTest#testFromByteArray()
     */
    @DisplayName("from(OutputStream) returns non-null")
    @Test
    void testFromOutputStream() {
        final ArrayByteOutput instance = ArrayByteOutput.from(new byte[0]);
        assertNotNull(instance);
    }
}
