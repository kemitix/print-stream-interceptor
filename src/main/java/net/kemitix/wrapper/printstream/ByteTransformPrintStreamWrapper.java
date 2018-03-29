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
 * Wrapper for {@link PrintStream} that can transform all bytes written, using a supplied transform {@link
 * java.util.function.Function}, and passes the result on to any inner wrapper or, if there isn't one, to the core
 * {@link PrintStream}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 * @see StringTransformPrintStreamWrapper
 */
class ByteTransformPrintStreamWrapper
        extends PassthroughPrintStreamWrapper {

    private final Function<Byte, Byte> transformer;

    /**
     * Constructor to wrap in existing PrintStream.
     *
     * @param original    the PrintStream to wrap
     * @param transformer the function to transform the byte
     */
    ByteTransformPrintStreamWrapper(
            final PrintStream original,
            @NonNull final Function<Byte, Byte> transformer
                                          ) {
        super(original);
        this.transformer = transformer;
    }

    @Override
    public final void write(final int b) {
        printStreamDelegate().write(transformer.apply((byte) b));
    }

    @Override
    public final void write(
            final byte[] buf,
            final int off,
            final int len
                           ) {
        new ByteBufferSegment(buf, off, len)
                .forEach(this::write);
    }
}
