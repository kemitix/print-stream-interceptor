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
import java.util.function.Predicate;

/**
 * Wrapper for {@link PrintStream} that tests bytes with a supplied {@link Predicate} before writing to any inner
 * wrapper or, if there isn't one, to the core {@link PrintStream}.
 *
 * <p>If the Predicate returns {@code false} for the byte, then the byte will not be written.</p>
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class ByteFilterPrintStreamWrapper
        extends PassthroughPrintStreamWrapper
        implements BytePrintStreamWrapper {

    private final Predicate<Byte> predicate;

    /**
     * Constructor to wrap in existing PrintStream.
     *
     * @param original  the PrintStream to wrap
     * @param predicate the predicate to apply to bytes
     */
    public ByteFilterPrintStreamWrapper(
            final PrintStream original,
            @NonNull final Predicate<Byte> predicate
                                       ) {
        super(original);
        this.predicate = predicate;
    }

    /**
     * Constructor to wrap in existing {@code Wrapper<PrintStream>}.
     *
     * @param wrapper   the wrapper to wrap
     * @param predicate the predicate to apply to bytes
     */
    public ByteFilterPrintStreamWrapper(
            final PrintStreamWrapper wrapper,
            @NonNull final Predicate<Byte> predicate
                                       ) {
        super(wrapper);
        this.predicate = predicate;
    }

    @Override
    public final void write(final int b) {
        if (predicate.test((byte) b)) {
            super.write(b);
        }
    }

    @Override
    public final void write(
            final byte[] buf,
            final int off,
            final int len
                           ) {
        BytePrintStreamWrapper.forEachByteInBuffer(buf, off, len, this::write);
    }
}
