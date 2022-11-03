package com.github.jinahya.bit.io;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 * An interface for writers need to write element count.
 *
 * @param <T> self type parameter.
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ReadsCount
 */
public interface WritesCount<T extends WritesCount<T>> {

    /**
     * Configures this writer to use specified consumer for writing the count of elements.
     *
     * @param countWriter the consumer accepts an {@code output} and a {@code count}, and writes the {@code count} to
     *                    the {@code output}.
     */
    default void setCountWriter(final ObjIntConsumer<? super BitOutput> countWriter) {
        Objects.requireNonNull(countWriter, "countWriter is null");
        try {
            final Field field = getClass().getDeclaredField("countWriter");
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(this, countWriter);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to set 'countWriter", roe); // NOSONAR
        }
    }

    /**
     * Configures this writer to use specified consumer for writing the count of elements, and returns this object.
     *
     * @param countWriter the consumer accepts an {@code output} and a {@code count}, and writes the {@code count} to
     *                    the {@code output}.
     * @return this object.
     */
    @SuppressWarnings({"unchecked"})
    default T countWriter(final ObjIntConsumer<? super BitOutput> countWriter) {
        setCountWriter(countWriter);
        return (T) this;
    }
}
