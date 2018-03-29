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
import net.kemitix.wrapper.Wrapper;

import java.io.PrintStream;
import java.util.Optional;

/**
 * Basic wrapper for {@link PrintStream} that simply passes all writes to the intercepted PrintStream, or to another
 * wrapper.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
class PassthroughPrintStreamWrapper extends PrintStream implements PrintStreamWrapper {

    private final PrintStreamWrapper wrapper;

    /**
     * Constructor to wrap a PrintStream.
     *
     * @param original the PrintStream to intercept
     */
    PassthroughPrintStreamWrapper(final PrintStream original) {
        super(original);
        wrapper = PrintStreamWrapper.wrap(original);
    }

    @Override
    public final Optional<PrintStreamWrapper> printStreamWrapperInner() {
        return Optional.of(wrapper);
    }

    /**
     * Write the byte to the wrapped PrintStream or PrintStreamWrapper.
     *
     * @param b The byte to be written
     *
     */
    @Override
    public void write(final int b) {
        wrapper.write(b);
    }

    /**
     * Write the contents of the byte array to the wrapped PrintStream of PrintStreamWrapper.
     *
     * @param buf A byte array
     * @param off Offset from which to start taking bytes
     * @param len Number of bytes to write
     */
    @Override
    public void write(
            @NonNull final byte[] buf,
            final int off,
            final int len
                     ) {
        wrapper.write(buf, off, len);
    }

    @Override
    public final PrintStream getWrapperSubject() {
        return wrapper.getWrapperSubject();
    }

    @Override
    public final Optional<Wrapper<PrintStream>> getInnerWrapper() {
        return Optional.of(wrapper);
    }
}
