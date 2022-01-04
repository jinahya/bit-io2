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

/**
 * Implementations of {@link BitReader}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class BitReaders {

    /**
     * Returns a wrapper reader which pre-reads a {@code boolean} flag indicating a {@code null} value.
     *
     * @param reader a reader to wrap.
     * @param <T>    value type parameter
     * @return a wrapper reader.
     * @deprecated Use {@link BitReader#nullable(BitReader)}
     */
    @Deprecated
    public static <T> BitReader<T> nullable(final BitReader<? extends T> reader) {
        return BitReader.nullable(reader);
    }

    private BitReaders() {
        throw new AssertionError("instantiation is not allowed");
    }
}
