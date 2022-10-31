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
 * Defines constants for bit-io.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;\
 */
final class BitIoConstants {

    static final String MESSAGE_INSTANTIATION_IS_NOT_ALLOWED = "instantiation is not allowed";

    static final String MESSAGE_UNSUPPORTED_ALREADY_NULLABLE = "unsupported; already nullable";

    static final String MESSAGE_UNSUPPORTED_NOT_SUPPOSED_TO_BE_INVOKED = "unsupported; not supposed to be invoked";

    private static final int SIZE_COUNT = 31;

    static final ToIntFunction<? super BitInput> COUNT_READER = i -> {
        try {
            return i.readInt(true, SIZE_COUNT);
        } catch (final IOException ioe) {
            throw new RuntimeException("failed to read a (compressed) count", ioe);
        }
    };

    static final ObjIntConsumer<? super BitOutput> COUNT_WRITER = (o, c) -> {
        try {
            o.writeInt(true, SIZE_COUNT, c);
        } catch (final IOException ioe) {
            throw new RuntimeException("failed to write a (compressed) count", ioe);
        }
    };

    static final ToIntFunction<? super BitInput> COUNT_READER_COMPRESSED = i -> {
        try {
            return BitIoUtils.readCountCompressed(i);
        } catch (final IOException ioe) {
            throw new RuntimeException("failed to read a (compressed) count", ioe);
        }
    };

    static final ObjIntConsumer<? super BitOutput> COUNT_WRITER_COMPRESSED = (o, c) -> {
        try {
            BitIoUtils.writeCountCompressed(o, c);
        } catch (final IOException ioe) {
            throw new RuntimeException("failed to write a (compressed) count", ioe);
        }
    };

    private BitIoConstants() {
        throw new AssertionError(MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
