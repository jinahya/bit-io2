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

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Supplier;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;

/**
 * A class for testing nullable instances.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class Nullable_Wr_Test {

    static <T> void test(final Supplier<? extends BitWriter<T>> writerSupplier,
                         final Supplier<? extends BitReader<T>> readerSupplier)
            throws IOException {
        final var actual = wr1u(o -> {
            writerSupplier.get().nullable().write(o, null);
            return i -> readerSupplier.get().nullable().read(i);
        });
        assertThat(actual).isNull();
    }
}
