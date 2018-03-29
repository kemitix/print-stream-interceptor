# PrintStream Wrapper

Wrappers for `PrintStream` with copy, filter and transform implementations.

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/573c34eda55e429aa261e02e30cbaa81)](https://www.codacy.com/app/kemitix/print-stream-wrapper?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=kemitix/print-stream-wrapper&amp;utm_campaign=Badge_Grade)

## Usage

Usage for the following (contrived) example:
```
println("...") ===> transform ===> copy ===> filter ---> [coreArray]
                                        \==> [copyArray]
```

* `copyArray` receives everything
* `coreArray` receives filtered output

```java
@Test
public void usage() {
        //given
        final OutputStream coreArray = new ByteArrayOutputStream();
        final OutputStream copyArray = new ByteArrayOutputStream();
        final PrintStream core = new PrintStream(coreArray);
        final PrintStream copy = new PrintStream(copyArray);

        final PrintStream printStream =
                PrintStreamWrapper.transform(
                        PrintStreamWrapper.copy(
                                PrintStreamWrapper.filter(core, (String o) -> o.contains("ERROR")),
                                copy),
                        (PrintStreamWrapper.StringTransform) String::toUpperCase);

        //when
        printStream.println("This is an error message");
        printStream.println("This is an ordinary message");
        //then
        assertThat(coreArray.toString()).contains("THIS IS AN ERROR MESSAGE");
        assertThat(copyArray.toString()).contains("THIS IS AN ERROR MESSAGE", "THIS IS AN ORDINARY MESSAGE");
}
```

See the `PrintStreamWrapper` interface for all static constructors available.