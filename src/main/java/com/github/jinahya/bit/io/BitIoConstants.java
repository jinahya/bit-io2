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
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

/**
 * Constants defined for reading/writing bits.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;\
 */
public final class BitIoConstants {

    static final String MESSAGE_INPUT_IS_NULL = "input is null";

    static final String MESSAGE_OUTPUT_IS_NULL = "output is null";

    static final String MESSAGE_INSTANTIATION_IS_NOT_ALLOWED = "instantiation is not allowed";

    static final String MESSAGE_UNSUPPORTED_ALREADY_NULLABLE = "unsupported; already nullable";

    static final String MESSAGE_UNSUPPORTED_NOT_SUPPOSED_TO_BE_INVOKED = "unsupported; not supposed to be invoked";

    /**
     * A function for reading a {@value Short#SIZE}-bit unsigned count value.
     *
     * @see BitIoUtils#readCountShort(BitInput)
     */
    public static final ToIntFunction<? super BitInput> COUNT_READER_SHORT = i -> {
        try {
            return BitIoUtils.readCountShort(i);
        } catch (final IOException ioe) {
            throw new RuntimeException("failed to read an uncompressed count", ioe); // NOSONAR
        }
    };

    /**
     * A consumer for writing a {@value Short#SIZE}-bit unsigned count value.
     *
     * @see BitIoUtils#writeCount(BitOutput, int)
     */
    public static final ObjIntConsumer<? super BitOutput> COUNT_WRITER_SHORT = (o, c) -> {
        try {
            BitIoUtils.writeCountShort(o, c);
        } catch (final IOException ioe) {
            throw new RuntimeException("failed to write an uncompressed count", ioe); // NOSONAR
        }
    };

    /**
     * A function for reading a {@code 31}-bit unsigned count value.
     *
     * @see BitIoUtils#readCount(BitInput)
     */
    public static final ToIntFunction<? super BitInput> COUNT_READER = i -> {
        try {
            return BitIoUtils.readCount(i);
        } catch (final IOException ioe) {
            throw new RuntimeException("failed to read an uncompressed count", ioe); // NOSONAR
        }
    };

    /**
     * A consumer for writing a {@code 31}-bit unsigned count value.
     *
     * @see BitIoUtils#writeCount(BitOutput, int)
     */
    public static final ObjIntConsumer<? super BitOutput> COUNT_WRITER = (o, c) -> {
        try {
            BitIoUtils.writeCount(o, c);
        } catch (final IOException ioe) {
            throw new RuntimeException("failed to write an uncompressed count", ioe); // NOSONAR
        }
    };

    /**
     * A function reads an unsigned count value in a compressed manner.
     *
     * @see BitIoUtils#readCountCompressed(BitInput)
     */
    public static final ToIntFunction<? super BitInput> COUNT_READER_COMPRESSED = i -> {
        try {
            return BitIoUtils.readCountCompressed(i);
        } catch (final IOException ioe) {
            throw new RuntimeException("failed to read a compressed count", ioe); // NOSONAR
        }
    };

    /**
     * A consumer writes given count value in a compressed manner.
     *
     * @see BitIoUtils#writeCountCompressed(BitOutput, int)
     */
    public static final ObjIntConsumer<? super BitOutput> COUNT_WRITER_COMPRESSED = (o, c) -> {
        try {
            BitIoUtils.writeCountCompressed(o, c);
        } catch (final IOException ioe) {
            throw new RuntimeException("failed to write a compressed count", ioe); // NOSONAR
        }
    };

    private BitIoConstants() {
        throw new AssertionError(MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
