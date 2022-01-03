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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A value writer for writing string values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StringReader
 */
public class StringWriter
        implements BitWriter<String> {

    /**
     * Creates a new instance for writing ASCII strings.
     *
     * @param maximumCharacters a maximum number of characters of a string; must be non-negative.
     * @param printableOnly     a flag for printable characters only; {@code true} for printable characters; {@code
     *                          false} otherwise.
     * @return a new instance.
     * @see StringReader#ascii(int, boolean)
     */
    public static StringWriter ascii(final int maximumCharacters, final boolean printableOnly) {
        if (maximumCharacters < 0) {
            throw new IllegalArgumentException("maximumCharacters(" + maximumCharacters + " is negative");
        }
        final int lengthSize = BitIoUtils.size(maximumCharacters);
        return new StringWriter(ByteArrayWriter.ascii(lengthSize, printableOnly), StandardCharsets.US_ASCII);
    }

    /**
     * Creates a new instance for writing UTF-8 strings.
     *
     * @return a new instance.
     * @see StringReader#utf8()
     */
    public static StringWriter utf8() {
        return new StringWriter(ByteArrayWriter.utf8(31), StandardCharsets.UTF_8);
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param delegate a writer for writing encoded bytes.
     * @param charset  a charset for encoding values.
     */
    public StringWriter(final ByteArrayWriter delegate, final Charset charset) {
        super();
        this.delegate = Objects.requireNonNull(delegate, "delegate is null");
        this.charset = Objects.requireNonNull(charset, "charset is null");
    }

    @Override
    public void write(final BitOutput output, final String value) throws IOException {
        delegate.write(output, value.getBytes(charset));
    }

    private final ByteArrayWriter delegate;

    private final Charset charset;
}
