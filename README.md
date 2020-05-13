# bit-io2

![Java CI with Maven](https://github.com/jinahya/bit-io2/workflows/Java%20CI%20with%20Maven/badge.svg)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/bit-io2/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/bit-io2)

A Java 8+ flavored version of [bit-io](https://github.com/jinahya/bit-io).

## How to use?

Add this module as a dependency. Check the [central](https://search.maven.org/search?q=g:com.github.jinahya%20a:bit-io2) the current version.

```xml
<dependency>
  <groupId>com.github.jinahya</groupId>
  <artifactId>bit-io2</artifactId>
  <version>LATEST</version>
</dependency>
```

## How it works?

Two interfaces defined for reading/writing non-octet aligned values and two more interfaces are defined for reading/writing octets from/to various sources/targets.

```
CLIENT <-read BitInput(Adapter)
                         <-read
                               ByteInput(Adapter)
                                           <-read ByteBuffer
                                                  DataInput
                                                  InputStream
                                                  ...
```

```
CLIENT write-> BitOutput(Adapter)
                          write-> ByteOutput(Adapter)
                                              write-> ByteBuffer
                                                      DataOutput
                                                      OutputStream
                                                      ...
```

Each `...Adapter` class accepts an instance of `Supplier<? extends T>` which means any byte sources/targets can be lazily initialized only when some bits are read or written.

See [Specifications](https://github.com/jinahya/bit-io2/wiki/Specifications) and [Recipes](https://github.com/jinahya/bit-io2/wiki/Recipes) for more information.
