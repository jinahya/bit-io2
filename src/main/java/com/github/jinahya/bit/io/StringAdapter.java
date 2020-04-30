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

import static com.github.jinahya.bit.io.BytesAdapter.unsignedBytesAdapter;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.Objects.requireNonNull;

public class StringAdapter implements ValueAdapter<String> {

    public static StringAdapter asciiAdapter(final int lengthSize) {
        return new StringAdapter(unsignedBytesAdapter(lengthSize, 7), US_ASCII);
    }

    public StringAdapter(final BytesAdapter delegate, final Charset charset) {
        super();
        this.delegate = requireNonNull(delegate, "delegate is null");
        this.charset = requireNonNull(charset, "charset is null");
    }

    @Override
    public void write(final BitOutput output, final String value) throws IOException {
        delegate.write(output, value.getBytes(charset));
    }

    @Override
    public String read(final BitInput input) throws IOException {
        return new String(delegate.read(input), charset);
    }

    protected final BytesAdapter delegate;

    protected final Charset charset;
}
