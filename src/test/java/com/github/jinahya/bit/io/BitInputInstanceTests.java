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
 * A class for testing instances of {@link BitInput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutputInstanceTests
 */
final class BitInputInstanceTests {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link BitInput#readBoolean()} method with specified instance.
     *
     * @param instance the instance.
     * @throws IOException if an I/O error occurs.
     * @see BitOutputInstanceTests#testByte(BitOutput)
     */
    static void testBoolean(final BitInput instance) throws IOException {
        final boolean value = requireNonNull(instance, "instance is null").readBoolean();
    }

    /**
     * Tests {@link BitInput#readByte(boolean, int)} method with specified instance.
     *
     * @param instance the instance.
     * @throws IOException if an I/O error occurs.
     * @see BitOutputInstanceTests#testByte(BitOutput)
     */
    static void testByte(final BitInput instance) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = BitIoTestValues.randomSizeForByte(unsigned);
        final byte value = requireNonNull(instance, "instance is null").readByte(unsigned, size);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests with specified instance.
     *
     * @param instance the instance.
     * @throws IOException if an I/O error occurs.
     */
    static void test(final BitInput instance) throws IOException {
        testBoolean(instance);
        testByte(instance);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private BitInputInstanceTests() {
        super();
    }
}
