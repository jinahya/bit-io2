package com.github.jinahya.bit.io;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder(access = AccessLevel.PACKAGE)
class BitIoAdapter {

    @Accessors(fluent = true)
    @Getter(AccessLevel.PACKAGE)
    private final BitInput input;

    @Accessors(fluent = true)
    @Getter(AccessLevel.PACKAGE)
    private final BitOutput output;
}
