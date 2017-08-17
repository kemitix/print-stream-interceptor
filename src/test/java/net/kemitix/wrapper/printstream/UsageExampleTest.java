/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Paul Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;
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
     * println("...") ===> passthrough ===> copy ===> filter ---> redirect =x=> [coreArray]
     *                                           \==> [copyArray]          \==> [redirectArray]
     * </pre>
     * <ul><li>copyArray receives everything</li> <li>redirectArray receives only what passes the filter</li>
     * <li>coreArray receives nothing</li></ul>
     */
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
                new StringFilterPrintStreamWrapper(redirectWrapper, o -> o.contains("error"));
        final Wrapper<PrintStream> copyWrapper = new CopyPrintStreamWrapper(filteredWrapper, copyTo);
        final Wrapper<PrintStream> passthroughWrapper = new PassthroughPrintStreamWrapper(copyWrapper);
        final PrintStream printStream = passthroughWrapper.asCore();
        printStream.println(message1);
        printStream.println(message2);
        //then
        assertThat(coreArray.toString()).contains("");
        assertThat(redirectArray.toString()).contains(message1);
        assertThat(copyArray.toString()).contains(message1, message2);
    }
}
