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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
class DummyBitInputParameterResolver
        implements ParameterResolver {

    private static class DummyBitInput
            implements BitInput {

        @Override
        public int readInt(final boolean unsigned, final int size) throws IOException {
            BitIoConstraints.requireValidSizeForInt(unsigned, size);
            final var value = ThreadLocalRandom.current().nextInt(256);
            if (unsigned) {
                return value >>> (Integer.SIZE - size);
            }
            return value >> (Integer.SIZE - size);
        }

        @Override
        public long align(final int bytes) throws IOException {
            return 0L;
        }
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == BitInput.class;
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return Mockito.spy(new DummyBitInput());
    }
}
