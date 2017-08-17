/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Paul Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kemitix.wrapper.printstream;

import lombok.NonNull;
import net.kemitix.wrapper.Wrapper;

import java.io.PrintStream;
import java.util.function.Predicate;

/**
 * Wrapper for {@link PrintStream} that tests Strings with a supplied {@link Predicate} before writing to any inner
 * wrapper or, if there isn't one, to the core {@link PrintStream}.
 *
 * <p>If the Predicate returns {@code false} for the String, then the String will not be written.</p>
 *
 * <p>N.B. only {@link #print(String)} and {@link #println(String)} will be checked against the Predicate. All other
 * {@code #write(*)}, {@code #print(*)} or {@code #println(*)} calls will always be written.</p>
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class StringFilterPrintStreamWrapper extends PassthroughPrintStreamWrapper {

    private final Predicate<String> predicate;

    /**
     * Constructor to wrap in existing PrintStream.
     *
     * @param core      the PrintStream to wrap
     * @param predicate the predicate to apply to strings
     */
    public StringFilterPrintStreamWrapper(final PrintStream core, @NonNull final Predicate<String> predicate) {
        super(core);
        this.predicate = predicate;
    }

    /**
     * Constructor to wrap in existing {@code Wrapper<PrintStream>}.
     *
     * @param wrapper   the wrapper to wrap
     * @param predicate the predicate to apply to strings
     */
    public StringFilterPrintStreamWrapper(
            final Wrapper<PrintStream> wrapper, @NonNull final Predicate<String> predicate
                                         ) {
        super(wrapper);
        this.predicate = predicate;
    }

    @Override
    public final void print(final String s) {
        if (predicate.test(s)) {
            super.print(s);
        }
    }

    @Override
    public final void println(final String s) {
        if (predicate.test(s)) {
            super.println(s);
        }
    }
}
