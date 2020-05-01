# bit-io2

[![javadoc](https://javadoc.io/badge2/com.github.jinahya/bit-io2/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/bit-io2)

A Java 8+ flavored version of [bit-io](https://github.com/jinahya/bit-io).

## Supported Types and Values

### Primitives

#### Numeric

##### Integral

##### `byte`, `short`, `int`, and `long`

There, for each type, are three methods for reading and (corresponding) three methods for writing.

|signature                      |description                 |notes                               |
|-------------------------------|----------------------------|------------------------------------|
|`r...(ZI)*`, `w...(ZI*)V`      |(signed or unsigned) `I`-bit|`I`: `1` ~ (`N` - (`Z` ? `1` : `0`))|
|`r...(I)*`, `w...(I*)V`        |`I`-bit signed              |                                    |
|`r...N()*`, `w...N(*)V`        |`N`-bit signed              |`N`: `8`, `16`, `32`, `64`          |
|`r...NLe()*`, `w...NLe(*)V`    |`N`-bit in little endian    |N/A with `byte`                     |
|`r...U...(I)*`, `w...U...(I*)V`|`I`-bit unsigned            |                                    |

##### `char`

Reads/writes values as (in maximum) `16`-bit unsigned `int`.

|signature                         |description      |notes          |
|----------------------------------|-----------------|---------------|
|`readChar(I)C`, `writeChar(I,C)V` |`I`-bit unsigned |`I`: `1` ~ `16`|
|`readChar16()C`, `writeChar16(C)V`|`16`-bit unsigned|               |

##### Floating Point

No methods defined for arbitrary number of bits.

|signature                             |description      |notes|
|--------------------------------------|-----------------|-----|
|`readFloat32()F`, `writeFloat32(F)V`  |`32`-bit `float` |     |
|`readDouble64()D`, `writeDouble64(D)V`|`64`-bit `double`|     |

#### boolean

Reads/writes just `1` bit; `0b1` for `true`, `0b0` for `false`.

|signature                           |description      |notes|
|------------------------------------|-----------------|-----|
|`readBoolean()Z`, `writeBoolean(Z)V`|`1`-bit `boolean`|     |

### References

For complex/composite object references, we can use instances of `ValueAdapter<T>`.

|signature                                                                      |description|notes|
|-------------------------------------------------------------------------------|-----------|-----|
|`<T>(Lc....ValueAdapter<? extends T>)T`, `<T>(L...ValueAdapter<? super T>, T)V`|           |     |

For example, let's say we have the `User` class.

```java
class User {
    String name;
    int age;
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

    private final StringAdapter nameAdapter = new StringAdapter(BytesAdapter.bytesAdapter8(8), UTF_8);
}
```

## Skipping and Aligning