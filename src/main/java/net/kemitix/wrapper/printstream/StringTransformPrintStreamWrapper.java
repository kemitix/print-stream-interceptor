/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Paul Campbell
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

import java.io.PrintStream;
import java.util.function.Function;

/**
 * Wrapper for {@link PrintStream} that can transform all String writes, using a supplied ?, and passes it on to any
 * inner wrapper or, if there isn't one, to the core {@link PrintStream}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class StringTransformPrintStreamWrapper extends PassthroughPrintStreamWrapper {

    private final Function<String, String> transformer;

    /**
     * Constructor to wrap in existing PrintStream.
     *
     * @param original    the PrintStream to wrap
     * @param transformer the function to transform the string
     */
    public StringTransformPrintStreamWrapper(
            final PrintStream original, @NonNull final Function<String, String> transformer
                                            ) {
        super(original);
        this.transformer = transformer;
    }

    /**
     * Constructor to wrap an existing {@code Wrapper<PrintStream>}.
     *
     * @param wrapper     the wrapper to wrap
     * @param transformer the function to transform the string
     */
    public StringTransformPrintStreamWrapper(
            final PrintStreamWrapper wrapper,
            @NonNull final Function<String, String> transformer
                                            ) {
        super(wrapper);
        this.transformer = transformer;
    }

    @Override
    public final void print(final String s) {
        printStreamDelegate().print(transformer.apply(s));
    }
}
