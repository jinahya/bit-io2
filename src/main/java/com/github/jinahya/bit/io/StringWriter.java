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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A class for writing string values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StringReader
 */
public class StringWriter
        extends FilterBitWriter<String, byte[]> {

    /**
     * Creates a new instance for writing {@link StandardCharsets#US_ASCII}-encoded strings in a compressed-manner.
     *
     * @param printableOnly a flag for printable characters only; {@code true} for printable characters; {@code false}
     *                      otherwise.
     * @return a new instance. a@see ByteArrayWriter#ascii31(boolean)
     */
    public static StringWriter ascii(final boolean printableOnly) {
        final ByteArrayWriter delegate = ByteArrayWriter.ascii31(printableOnly);
        return new StringWriter(delegate, StandardCharsets.US_ASCII);
    }

    /**
     * Creates a new instance for writing {@link StandardCharsets#UTF_8}-encoded strings in a compressed-manner.
     *
     * @return a new instance.
     * @see ByteArrayWriter#utf831()
     */
    public static StringWriter utf8() {
        final ByteArrayWriter delegate = ByteArrayWriter.utf831();
        return new StringWriter(delegate, StandardCharsets.UTF_8);
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param delegate a writer for writing encoded bytes.
     * @param charset  a charset for encoding a value.
     * @see ByteArrayWriter#of318()
     */
    public StringWriter(final ByteArrayWriter delegate, final Charset charset) {
        super(delegate);
        this.charset = Objects.requireNonNull(charset, "charset is null");
    }

    @Override
    protected byte[] map(final String value) {
        return value.getBytes(charset);
    }

    private final Charset charset;
}
