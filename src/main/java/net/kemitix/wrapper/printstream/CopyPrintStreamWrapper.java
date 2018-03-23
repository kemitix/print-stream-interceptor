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
 * Wrapper for {@link PrintStream} that copies all writes to the supplied PrintStream and to any inner wrapper or, if
 * there isn't one, to the core {@link PrintStream}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class CopyPrintStreamWrapper extends PassthroughPrintStreamWrapper {

    private final PrintStream copyTo;

    /**
     * Constructor to wrap in existing PrintStream.
     *
     * @param core   the PrintStream to wrap
     * @param copyTo the PrintStream to copy to
     */
    public CopyPrintStreamWrapper(final PrintStream core, @NonNull final PrintStream copyTo) {
        super(core);
        this.copyTo = copyTo;
    }

    /**
     * Constructor to wrap an existing {@code Wrapper<PrintStream>}.
     *
     * @param wrapper the wrapper to wrap
     * @param copyTo  the PrintStream to copy to
     */
    public CopyPrintStreamWrapper(
            final PrintStreamWrapper wrapper,
            @NonNull final PrintStream copyTo
                                 ) {
        super(wrapper);
        this.copyTo = copyTo;
    }

    @Override
    public final void write(final int b) {
        super.write(b);
        copyTo.write(b);
    }

    @Override
    public final void write(final byte[] buf, final int off, final int len) {
        super.write(buf, off, len);
        copyTo.write(buf, off, len);
    }
}
