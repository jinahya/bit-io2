package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
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

import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

final class ByteIoTestUtilities {

    static <R> R wr(final Supplier<? extends ByteOutput> outputSupplier,
                    final Supplier<? extends ByteInput> inputSupplier,
                    final Function<? super ByteOutput, ? extends Function<? super ByteInput, ? extends R>> f1)
            throws IOException {
        final Function<? super ByteInput, ? extends R> f2;
        ByteOutput output = outputSupplier.get();
        f2 = f1.apply(output);
        ByteInput input = inputSupplier.get();
        return f2.apply(input);
    }

    static <R> R wrv(
            final CheckedFunction0<? extends ByteOutput> outputSupplier,
            final CheckedFunction0<? extends ByteInput> inputSupplier,
            final CheckedFunction1<? super ByteOutput, ? extends CheckedFunction1<? super ByteInput, ? extends R>> f1)
            throws IOException {
        return wr(
                () -> outputSupplier.unchecked().apply(),
                () -> inputSupplier.unchecked().apply(),
                o -> {
                    final CheckedFunction1<? super ByteInput, ? extends R> f2 = f1.unchecked().apply(o);
                    return i -> f2.unchecked().apply(i);
                }
        );
    }

    private ByteIoTestUtilities() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
