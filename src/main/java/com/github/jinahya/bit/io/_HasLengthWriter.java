package com.github.jinahya.bit.io;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

interface _HasLengthWriter<T extends _HasLengthWriter<T>> {

    default void setLengthWriter(final ObjIntConsumer<? super BitOutput> lengthWriter) {
        Objects.requireNonNull(lengthWriter, "lengthWriter is null");
        try {
            final Field field = getClass().getDeclaredField("lengthWriter");
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(this, lengthWriter);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to set 'lengthWriter", roe);
        }
    }

    @SuppressWarnings({"unchecked"})
    default T lengthWriter(final ObjIntConsumer<? super BitOutput> lengthWriter) {
        setLengthWriter(lengthWriter);
        return (T) this;
    }
}
