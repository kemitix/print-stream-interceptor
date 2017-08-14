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

import lombok.Getter;
import lombok.NonNull;
import net.kemitix.wrapper.Wrapper;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Basic wrapper for {@link PrintStream} that simply passes all writes to the intercepted PrintStream, or to another
 * wrapper.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class PassthroughPrintStreamWrapper extends PrintStream implements Wrapper<PrintStream> {

    @Getter
    private final PrintStream core;

    private final AtomicReference<Wrapper<PrintStream>> innerWrapper = new AtomicReference<>();

    /**
     * Constructor to intercept a PrintStream.
     *
     * @param original the PrintStream to intercept
     */
    public PassthroughPrintStreamWrapper(final PrintStream original) {
        super(original);
        this.core = original;
    }

    /**
     * Constructor to intercept in existing {@code Wrapper<PrintStream>}.
     *
     * @param interceptor the wrapper to intercept
     */
    public PassthroughPrintStreamWrapper(final Wrapper<PrintStream> interceptor) {
        super(Objects.requireNonNull(interceptor, "wrapper").getCore());
        this.core = interceptor.getCore();
        this.innerWrapper.set(interceptor);
    }

    /**
     * Writes the specified byte to this stream.
     *
     * <p>If the byte is a newline and automatic flushing is enabled then the flush method will be invoked.</p>
     *
     * <p>Note that the byte is written as given; to write a character that will be translated according to the
     * platform's default character encoding, use the print(char) or println(char) methods.</p>
     *
     * <p>This implementation passes the byte, unmodified, to the intercepted {@link PrintStream} or {@code
     * Wrapper<PrintStream>}.</p>
     *
     * @param b The byte to be written
     *
     * @see #print(char)
     * @see #println(char)
     */
    @Override
    public void write(final int b) {
        if (innerWrapper.get() != null) {
            innerWrapper.get()
                        .asCore()
                        .write(b);
        } else {
            core.write(b);
        }
    }

    /**
     * Writes len bytes from the specified byte array starting at offset off to this stream.
     *
     * <p>If automatic flushing is enabled then the flush method will be invoked.</p>
     *
     * <p>Note that the bytes will be written as given; to write characters that will be translated according to the
     * platform's default character encoding, use the print(char) or println(char) methods.</p>
     *
     * <p>This implementation passes the bytes, unmodified, to the intercepted {@link PrintStream} or {@code
     * Wrapper<PrintStream>}.</p>
     *
     * @param buf A byte array
     * @param off Offset from which to start taking bytes
     * @param len Number of bytes to write
     */
    @Override
    public void write(@NonNull final byte[] buf, final int off, final int len) {
        if (innerWrapper.get() != null) {
            innerWrapper.get()
                        .asCore()
                        .write(buf, off, len);
        } else {
            core.write(buf, off, len);
        }
    }

    @Override
    public final Optional<Wrapper<PrintStream>> findInnerWrapper() {
        return Optional.ofNullable(innerWrapper.get());
    }

    @Override
    public final void remove(@NonNull final Wrapper<PrintStream> wrapper) {
        if (innerWrapper.compareAndSet(wrapper, null)) {
            return;
        }
        Optional.ofNullable(innerWrapper.get())
                .ifPresent(wrapped -> wrapped.remove(wrapper));
    }

    @Override
    public final PrintStream asCore() {
        return this;
    }
}
