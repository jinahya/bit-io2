package com.github.jinahya.bit.io;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.ToIntFunction;

/**
 * An interface for reader need to read element count.
 *
 * @param <T> self type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see WritesCount
 */
public interface ReadsCount<T extends ReadsCount<T>> {

    /**
     * Configures to use specified function for reading the {@code count} of elements.
     *
     * @param countReader the function applies with a {@code input} and reads the {@code count}.
     */
    default void setCountReader(final ToIntFunction<? super BitInput> countReader) {
        Objects.requireNonNull(countReader, "countReader is null");
        try {
            final Field field = getClass().getDeclaredField("countReader");
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(this, countReader);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to set 'countReader", roe);
        }
    }

    /**
     * Configures to use specified function for reading the {@code count} of elements, and returns this object.
     *
     * @param countReader the function applies with a {@code input} and reads the {@code count}.
     * @return this object.
     */
    @SuppressWarnings({"unchecked"})
    default T countReader(final ToIntFunction<? super BitInput> countReader) {
        setCountReader(countReader);
        return (T) this;
    }
}
