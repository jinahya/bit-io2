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
import java.util.Objects;
import java.util.function.ObjLongConsumer;

class CountWriter
        implements LongWriter {

    public CountWriter(final ObjLongConsumer<? super BitOutput> function) {
        super();
        this.function = Objects.requireNonNull(function, "function is null");
    }

    @Override
    public void writeLong(final BitOutput output, final long value) throws IOException {
        BitIoObjects.requireNonNullOutput(output);
        BitIoObjects.requireNonNegativeValue(value);
        function.accept(output, value);
    }

    private final ObjLongConsumer<? super BitOutput> function;
}
