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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeChar;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeShort;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Random values for both {@link BitInput} and {@link BitOutput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
final class BitIoTestValues {

    // ------------------------------------------------------------------------------------------------------------ byte

    /**
     * Returns a random {@code size} argument value for both {@link BitInput#readByte(int)} method and {@link
     * BitOutput#writeByte(int, byte)} method.
     *
     * @return a random value of {@code size} argument for {@code byte} values.
     */
    static int randomSizeForByte() {
        return requireValidSizeByte(false, current().nextInt(Byte.SIZE) + 1);
    }

    static int randomSizeForUnsignedByte() {
        return requireValidSizeInt(true, current().nextInt(1, Byte.SIZE)); // 1 ~ 31
    }

    static int randomSizeForByte(final boolean unsigned) {
        return unsigned ? randomSizeForUnsignedByte() : randomSizeForByte();
    }

    static byte randomValueForByte(final int size) {
        return (byte) (current().nextInt() >> (Integer.SIZE - requireValidSizeByte(false, size)));
    }

    static byte randomValueForUnsignedByte(final int size) {
        return (byte) ((current().nextInt() >>> 1) >> (Integer.SIZE - requireValidSizeInt(true, size)));
    }

    static byte randomValueForByte(final boolean unsigned, final int size) {
        return unsigned ? randomValueForUnsignedByte(size) : randomValueForByte(size);
    }

    // ----------------------------------------------------------------------------------------------------------- short
    static int randomSizeForShort() {
        return requireValidSizeShort(false, current().nextInt(Short.SIZE) + 1); // 1 - 16
    }

    static int randomSizeForUnsignedShort() {
        return requireValidSizeInt(true, current().nextInt(1, Short.SIZE)); // 1 ~ 16
    }

    static int randomSizeForShort(final boolean unsigned) {
        return unsigned ? randomSizeForUnsignedShort() : randomSizeForShort();
    }

    static short randomValueForShort(final int size) {
        return (short) (current().nextInt() >> (Integer.SIZE - requireValidSizeShort(false, size)));
    }

    static short randomValueForUnsignedShort(final int size) {
        return (short) ((current().nextInt() >>> 1) >> (Integer.SIZE - requireValidSizeInt(true, size)));
    }

    static short randomValueForShort(final boolean unsigned, final int size) {
        return unsigned ? randomValueForUnsignedShort(size) : randomValueForShort(size);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    static int randomSizeForInt() {
        return requireValidSizeInt(false, current().nextInt(Integer.SIZE) + 1); // 1 ~ 32
    }

    static int randomSizeForUnsignedInt() {
        return requireValidSizeInt(true, current().nextInt(1, Integer.SIZE)); // 1 ~ 31
    }

    static int randomSizeForInt(final boolean unsigned) {
        return unsigned ? randomSizeForUnsignedInt() : randomSizeForInt();
    }

    static int randomValueForInt(final int size) {
        return current().nextInt() >> (Integer.SIZE - requireValidSizeInt(false, size));
    }

    static int randomValueForUnsignedInt(final int size) {
        return (current().nextInt() >>> 1) >> (Integer.SIZE - requireValidSizeInt(true, size));
    }

    static int randomValueForInt(final boolean unsigned, final int size) {
        return unsigned ? randomValueForUnsignedInt(size) : randomValueForInt(size);
    }

    // ------------------------------------------------------------------------------------------------------------ long
    static int randomSizeForLong() {
        return requireValidSizeLong(false, current().nextInt(Long.SIZE) + 1); // 1 ~ 64
    }

    static int randomSizeForUnsignedLong() {
        return requireValidSizeLong(true, current().nextInt(1, Long.SIZE)); // 1 ~ 63
    }

    static int randomSizeForLong(final boolean unsigned) {
        return unsigned ? randomSizeForUnsignedLong() : randomSizeForLong();
    }

    static long randomValueForLong(final int size) {
        return current().nextLong() >> (Long.SIZE - requireValidSizeLong(false, size));
    }

    static long randomValueForUnsignedLong(final int size) {
        return (current().nextLong() >>> 1) >> (Long.SIZE - requireValidSizeLong(true, size));
    }

    static long randomValueForLong(final boolean unsigned, final int size) {
        return unsigned ? randomValueForUnsignedLong(size) : randomValueForLong(size);
    }

    // ------------------------------------------------------------------------------------------------------------ char
    static int randomSizeForChar() {
        return requireValidSizeChar(current().nextInt(Character.SIZE) + 1); // 1 ~ 16
    }

    static char randomValueForChar(final int size) {
        return (char) (current().nextInt() >>> (Integer.SIZE - requireValidSizeChar(size)));
    }

    static char randomValueForChar16() {
        return randomValueForChar(Character.SIZE);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoTestValues() {
        super();
    }
}
