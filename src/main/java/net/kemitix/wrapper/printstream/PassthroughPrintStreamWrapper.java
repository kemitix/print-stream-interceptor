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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Basic wrapper for {@link PrintStream} that simply passes all writes to the intercepted PrintStream, or to another
 * wrapper.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class PassthroughPrintStreamWrapper extends PrintStream implements PrintStreamWrapper {

    private final PrintStreamWrapper wrapper;

    /**
     * Constructor to wrap a PrintStream.
     *
     * @param original the PrintStream to intercept
     */
    public PassthroughPrintStreamWrapper(final PrintStream original) {
        super(original);
        wrapper = PrintStreamWrapper.wrap(original);
    }

    /**
     * Constructor to wrap an existing {@code Wrapper<PrintStream>}.
     *
     * @param object the wrapper to wrap
     */
    public PassthroughPrintStreamWrapper(final PrintStreamWrapper object) {
        super(Objects.requireNonNull(object, "wrapper")
                      .wrapperSubject());
        wrapper = PrintStreamWrapper.wrap(object);
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

    /**
     * Scan the buffer, from off to len, and give it to the byteConsumer.
     *
     * @param buf          the buffer to process
     * @param off          the offset within the buffer to begin
     * @param len          the number of bytes to process
     * @param byteConsumer the consumer to process each byte
     */
    protected final void forEachByteInBuffer(
            final byte[] buf,
            final int off,
            final int len,
            final Consumer<Byte> byteConsumer
                                            ) {
        if (len < 0) {
            throw new IndexOutOfBoundsException(
                    String.format("buf.length: %d, off: %d, len: %d", buf.length, off, len));
        }
        for (int i = 0; i < len; i++) {
            byteConsumer.accept(buf[off + i]);
        }
    }

    @Override
    public final PrintStream wrapperSubject() {
        return wrapper.wrapperSubject();
    }

    @Override
    public final Optional<Wrapper<PrintStream>> wrapperInner() {
        return Optional.of(wrapper);
    }
}
