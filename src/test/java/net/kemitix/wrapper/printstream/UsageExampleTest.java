/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2018 Paul Campbell
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kemitix.wrapper.printstream;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test demonstration usage for README.md.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class UsageExampleTest {

    /**
     * Usage for the following (contrived) example:
     * <pre>
     * println("...") ===> transform ===> copy ===> filter ---> redirect =x=> [coreArray]
     *                                           \==> [copyArray]          \==> [redirectArray]
     * </pre>
     * <ul><li>copyArray receives everything</li> <li>redirectArray receives only what passes the filter</li>
     * <li>coreArray receives nothing</li></ul>
     */
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
}
