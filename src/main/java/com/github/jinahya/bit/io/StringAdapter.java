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

import static java.util.Objects.requireNonNull;

/**
 * A value adapter for reading/writing string values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class StringAdapter
        implements ValueAdapter<String> {

    static StringAdapter ascii(final int lengthSize) {
        return new StringAdapter(new BytesAdapterAscii(lengthSize), StandardCharsets.US_ASCII);
    }

    static StringAdapter asciiPrintable(final int lengthSize) {
        return new StringAdapter(new BytesAdapterAsciiPrintable(lengthSize), StandardCharsets.US_ASCII);
    }

    /**
     * Creates a new instance for reading/writing {@link java.nio.charset.StandardCharsets#US_ASCII US-ASCII} strings.
     *
     * @param lengthSize    the number of bits for the length of encoded bytes.
     * @param printableOnly a flag for printable({@code [0x20..0x7E]}) characters only.
     * @return a new instance.
     */
    public static StringAdapter ascii(final int lengthSize, final boolean printableOnly) {
        if (printableOnly) {
            return asciiPrintable(lengthSize);
        }
        return ascii(lengthSize);
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param delegate an adapter for reading/writing encoded bytes.
     * @param charset  a charset for encoding/decoding values.
     */
    public StringAdapter(final BytesAdapter delegate, final Charset charset) {
        super();
        this.delegate = requireNonNull(delegate, "delegate is null");
        this.charset = requireNonNull(charset, "charset is null");
    }

    @Override
    public String read(final BitInput input) throws IOException {
        return new String(delegate.read(input), charset);
    }

    @Override
    public void write(final BitOutput output, final String value) throws IOException {
        delegate.write(output, value.getBytes(charset));
    }

    private final BytesAdapter delegate;

    private final Charset charset;
}
