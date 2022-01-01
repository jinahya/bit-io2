package com.github.jinahya.bit.io;

/**
 * A class for reading an array of unsigned bytes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class ByteArrayAdapterUnsigned
        extends ByteArrayAdapter {

    /**
     * Creates a new instance.
     *
     * @param lengthSize  the number of bits for the length of the array; between {@code 1} (inclusive) and {@value
     *                    Integer#SIZE} (exclusive).
     * @param elementSize the number of bits for each element in the array; between {@code 1} (inclusive) and {@value
     *                    Byte#SIZE} (exclusive).
     */
    ByteArrayAdapterUnsigned(final int lengthSize, final int elementSize) {
        super(new ByteArrayReaderUnsigned(lengthSize, elementSize),
              new ByteArrayWriterUnsigned(lengthSize, elementSize));
    }
}
