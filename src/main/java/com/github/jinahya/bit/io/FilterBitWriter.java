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

import java.util.Objects;

/**
 * An abstract class for filtering another writer.
 *
 * @param <T> value type parameter
 * @param <U> filtered value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see FilterBitReader
 */
public abstract class FilterBitWriter<T, U>
        implements BitWriter<T> {

    /**
     * Creates a new instance which wraps specified writer.
     *
     * @param writer the writer to wrap.
     */
    protected FilterBitWriter(final BitWriter<? super U> writer) {
        super();
        this.writer = Objects.requireNonNull(writer, "writer is null");
    }

    /**
     * Returns the writer wrapped by this writer.
     *
     * @return the writer wrapped by this writer.
     */
    protected BitWriter<? super U> getWriter() {
        return writer;
    }

    /**
     * The writer wrapped by this writer.
     */
    private final BitWriter<? super U> writer;
}
