# bit-io2

[![javadoc](https://javadoc.io/badge2/com.github.jinahya/bit-io2/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/bit-io2)

A Java 8+ flavored version of [bit-io](https://github.com/jinahya/bit-io).

## How it works?

There is no magic. The module just reads/writes values of requested number of bits from/into octets and there should be adapters for reading/writing octets from/to any given sources/targets.

```
CLIENT <-read- BitInput
               BitInputAdapter <-read- ByteInput
                                       ByteInputAdapter <-read-- (byte[]
                                                                  ByteBuffer
                                                                  DataInput
                                                                  InputStream
                                                                  RandomAccessFile
                                                                  ...)
```

```
CLIENT -write-> BitOutput
                BitOutputAdapter -write-> ByteOutput
                                          ByteOutputAdapter -write-> (byte[]
                                                                      ByteBuffer
                                                                      DataOutput
                                                                      OutputStream
                                                                      RandomAccessFile
                                                                      ...)
```

These `...Adapter` classes which each implements top-level interfaces accept an instance of `Supplier<? extends T>` which means any byte sources/targets can be lazily initialized only when some bits are requested to be read/written. 

## Supported Types and Values

### Primitive Type

All primitive types can be handled.

#### Numeric Type

##### Integral Type

###### `byte`, `short`, `int`, and `long`

There, for each type, are three methods for reading and (corresponding) three methods for writing.

|signature                      |description                 |notes                               |
|-------------------------------|----------------------------|------------------------------------|
|`r...(ZI)*`, `w...(ZI*)V`      |(signed or unsigned) `I`-bit|`I`: `1` ~ (`N` - (`Z` ? `1` : `0`))|
|`r...(I)*`, `w...(I*)V`        |`I`-bit signed              |                                    |
|`r...N()*`, `w...N(*)V`        |`N`-bit signed              |`N`: `8`, `16`, `32`, `64`          |
|`r...NLe()*`, `w...NLe(*)V`    |`N`-bit in Little Endian    |N/A with `byte`                     |
|`r...U...(I)*`, `w...U...(I*)V`|`I`-bit unsigned            |                                    |

####### How a signed integral value of `I`-bit is read/written?

Signed values composite with the first bit as the sign bit and lower `I-1` bits.
```
 S              | -- lower I-1 bits -- |
 xxxxxxxx xxxxxxxx ... xxxxxxxx xxxxxxxx
```

####### How an unsigned integral value of `I`-bit is read/written?

Unsigned values are simply processed with their lower `I`-bits.

```
                  | -- lower I bits -- |
 xxxxxxxx xxxxxxxx ... xxxxxxxx xxxxxxxx
```

###### `char`

Reads/writes values as (in maximum) `16`-bit unsigned `int`.

|signature                         |description      |notes          |
|----------------------------------|-----------------|---------------|
|`readChar(I)C`, `writeChar(I,C)V` |`I`-bit unsigned |`I`: `1` ~ `16`|
|`readChar16()C`, `writeChar16(C)V`|`16`-bit unsigned|               |

##### Floating Point Type

No methods defined for arbitrary number of bits.

|signature                             |description      |notes|
|--------------------------------------|-----------------|-----|
|`readFloat32()F`, `writeFloat32(F)V`  |`32`-bit `float` |     |
|`readDouble64()D`, `writeDouble64(D)V`|`64`-bit `double`|     |

#### `boolean`

Reads/writes just `1` bit; `0b1` for `true`, `0b0` for `false`.

|signature                           |description      |notes|
|------------------------------------|-----------------|-----|
|`readBoolean()Z`, `writeBoolean(Z)V`|`1`-bit `boolean`|     |

### Reference Type

No capabilities for binding directly to reference types. (I hope, someday, I can make one.)  

Instead, we can use `ValueAdapter<T>`.

|signature                                                                      |description|notes|
|-------------------------------------------------------------------------------|-----------|-----|
|`<T>(Lc....ValueAdapter<? extends T>)T`, `<T>(L...ValueAdapter<? super T>T)V`|           |     |

For example, let's say we have a `User` class.

```java
class User {
    String name; // say, 127 bytes in maximum
    @Max(127) @PositiveOrZero int age;
}
```

Now we can create an adapter class looks like this.

```java
class UserAdapter extends ValueAdapter<User> {

    @Override public User read(BitInput input) throws IOException {
        User user = new User();
        user.name = nameAdapter.read(input);
        user.age = input.readUnsignedInt(7);
    }

    @Override public void write(BitOutput output, User value) throws IOException {
        nameAdapter.write(output, value.name);
        output.writeUnsignedInt(7, value.age);
    }

    private final StringAdapter nameAdapter = new StringAdapter(bytesAdapter8(8), UTF_8);
}
```

And we can use it like this.

```java
ValueAdapter<User> adapter = new UserAdapter();
User user = bitInput.read(adapter);
bitOutput.write(adapter, user);
```

## Skipping and Aligning

### skipping

You can skip a positive number of bits.

|signature             |description|notes|
|----------------------|-----------|-----|
|`skip(I)V`, `skip(I)V`|           |     |

### aligning

You can align the stream for a positive number of bytes by padding/discarding required number of bits. 

|signature               |description|notes                                                   |
|------------------------|-----------|--------------------------------------------------------|
|`align(I)L`, `align(I)L`|           |returns number of bits (padded/discarded) while aligning|
