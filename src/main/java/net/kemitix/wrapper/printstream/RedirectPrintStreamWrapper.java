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

/**
 * Wrapper for {@link PrintStream} that Redirects all writes to the supplied PrintStream and ignores any inner
 * wrapper and the core {@link PrintStream}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
class RedirectPrintStreamWrapper extends PassthroughPrintStreamWrapper {

    private final PrintStream redirectTo;

    /**
     * Constructor to intercept in existing PrintStream.
     *
     * @param core       the PrintStream to wrap
     * @param redirectTo the PrintStream to redirect writes to
     *
     * @deprecated Use {@link PrintStreamWrapper#redirect(PrintStream, PrintStream)}
     */
    @Deprecated
    RedirectPrintStreamWrapper(final PrintStream core, @NonNull final PrintStream redirectTo) {
        super(core);
        this.redirectTo = redirectTo;
    }

    /**
     * Constructor to intercept in existing {@code Wrapper<PrintStream>}.
     *
     * @param wrapper    the wrapper to wrap
     * @param redirectTo the PrintStream to redirect writes to
     *
     * @deprecated Use {@link PrintStreamWrapper#redirect(PrintStreamWrapper, PrintStream)}
     */
    @Deprecated
    RedirectPrintStreamWrapper(
            final PrintStreamWrapper wrapper,
            @NonNull final PrintStream redirectTo
                                     ) {
        super(wrapper);
        this.redirectTo = redirectTo;
    }

    @Override
    public final void write(final int b) {
        redirectTo.write(b);
    }

    @Override
    public final void write(final byte[] buf, final int off, final int len) {
        redirectTo.write(buf, off, len);
    }
}
