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

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * A class for testing instances of {@link BitOutput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInputInstanceTests
 */
final class BitOutputInstanceTests {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Test {@link BitOutput#writeBoolean(boolean)} method with specified instance.
     *
     * @param instance the instance.
     * @throws IOException if an I/O error occurs.
     * @see BitInputInstanceTests#testBoolean(BitInput)
     */
    static void testBoolean(final BitOutput instance) throws IOException {
        requireNonNull(instance, "instance is null").writeBoolean(current().nextBoolean());
    }

    /**
     * Tests {@link BitOutput#writeByte(boolean, int, byte)} method with specified instance.
     *
     * @param instance the instance.
     * @throws IOException if an I/O error occurs.
     * @see BitInputInstanceTests#testByte(BitInput)
     */
    static void testByte(final BitOutput instance) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = BitIoTestValues.randomSizeForByte(unsigned);
        final byte value = BitIoTestValues.randomValueForByte(unsigned, size);
        requireNonNull(instance, "instance is null").writeByte(unsigned, size, value);
    }

    /**
     * Tests with specified instance.
     *
     * @param instance the instance.
     * @throws IOException if an I/O error occurs.
     * @see BitInputInstanceTests#test(BitInput)
     */
    static void test(final BitOutput instance) throws IOException {
        testBoolean(requireNonNull(instance, "instance is null"));
        testBoolean(requireNonNull(instance, "instance is null"));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private BitOutputInstanceTests() {
        super();
    }
}
