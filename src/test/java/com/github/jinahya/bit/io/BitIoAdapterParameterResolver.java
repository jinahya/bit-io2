package com.github.jinahya.bit.io;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

class BitIoAdapterParameterResolver
        implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == BitIoAdapter.class;
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return BitIoAdapter.builder()
                .input(BitInputAdapter.from(ByteInputTestUtilities.white()))
                .output(BitOutputAdapter.from(ByteOutputTestUtilities.black()))
                .build();
    }
}
