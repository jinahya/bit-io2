package com.github.jinahya.bit.io;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.ToIntFunction;

interface _HasLengthReader<T extends _HasLengthReader<T>> {

    default void setLengthReader(final ToIntFunction<? super BitInput> lengthReader) {
        Objects.requireNonNull(lengthReader, "lengthReader is null");
        try {
            final Field field = getClass().getDeclaredField("lengthReader");
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(this, lengthReader);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to set 'lengthReader", roe);
        }
    }

    @SuppressWarnings({"unchecked"})
    default T lengthReader(final ToIntFunction<? super BitInput> lengthReader) {
        setLengthReader(lengthReader);
        return (T) this;
    }
}
