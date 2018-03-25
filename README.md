# PrintStream Wrapper

Wrappers for `PrintStream` with copy, redirect, filter and passthrough implementations.

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/573c34eda55e429aa261e02e30cbaa81)](https://www.codacy.com/app/kemitix/print-stream-wrapper?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=kemitix/print-stream-wrapper&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/kemitix/print-stream-wrapper/badge.svg?branch=master)](https://coveralls.io/github/kemitix/print-stream-wrapper?branch=master)
[![codecov](https://codecov.io/gh/kemitix/print-stream-wrapper/branch/master/graph/badge.svg)](https://codecov.io/gh/kemitix/print-stream-wrapper)

## Usage

Usage for the following (contrived) example:
```
println("...") ===> passthrough ===> copy ===> filter ---> redirect =x=> [coreArray]
                                          \==> [copyArray]          \==> [redirectArray]
```

* `copyArray` receives everything
* `redirectArray` receives only what passes the filter
* `coreArray` receives nothing

```java
@Test
public void usage() {
    //given
    final OutputStream coreArray = new ByteArrayOutputStream();
    final PrintStream core = new PrintStream(coreArray);
    final OutputStream redirectArray = new ByteArrayOutputStream();
    final PrintStream redirectTo = new PrintStream(redirectArray);
    final OutputStream copyArray = new ByteArrayOutputStream();
    final PrintStream copyTo = new PrintStream(copyArray);
    final String message1 = "This is an error message";
    final String message2 = "This is an ordinary message";
    //when
    final Wrapper<PrintStream> redirectWrapper = new RedirectPrintStreamWrapper(core, redirectTo);
    final Wrapper<PrintStream> filteredWrapper =
            new StringFilteredPrintStreamWrapper(redirectWrapper, o -> o.contains("error"));
    final Wrapper<PrintStream> copyWrapper = new CopyPrintStreamWrapper(filteredWrapper, copyTo);
    final Wrapper<PrintStream> passthroughWrapper = PrintStreamWrapper.passthrough(copyWrapper);
    final PrintStream printStream = passthroughWrapper.asCore();
    printStream.println(message1);
    printStream.println(message2);
    //then
    assertThat(coreArray.toString()).contains("");
    assertThat(redirectArray.toString()).contains(message1);
    assertThat(copyArray.toString()).contains(message1, message2);
}
```

### Wrappers

The following Wrappers are available:

* PassthroughPrintStreamWrapper - does nothing (is a base for all other PrintStreamWrappers)
* RedirectPrintStreamWrapper - writes all output to alternate PrintStream
* StringFilterPrintStreamWrapper - uses a Predicate to filter String writes
* ByteFilterPrintStreamWrapper - uses a Predicate to filter byte writes
* CopyPrintStreamWrapper - copies writes to another PrintStream
* StringTransformPrintStreamWrapper - uses a Function to modify String writes
* ByteTransformPrintStreamWrapper - uses a Function to modify byte writes
