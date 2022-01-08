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
 * A utility class related to {@link BitWriter}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class BitWriters {

    /**
     * Returns a writer which handles nullable values.
     *
     * @param writer a writer to wrap.
     * @param <T>    value type parameter
     * @return a wrapper writer.
     * @deprecated Use {@link BitWriter#nullable(BitWriter)}
     */
    @Deprecated
    public static <T> BitWriter<T> nullable(final BitWriter<? super T> writer) {
        return BitWriter.nullable(writer);
    }

    private BitWriters() {
        throw new AssertionError("instantiation is not allowed");
    }
}
