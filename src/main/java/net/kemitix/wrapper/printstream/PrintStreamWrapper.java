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

import net.kemitix.wrapper.Wrapper;

import java.io.PrintStream;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@link Wrapper} for handling {@link PrintStream}.
 *
 * <p>Provides access to the {@link PrintStream#write(int)} and {@link PrintStream#write(byte[], int, int)} through
 * the {@link Wrapper}.</p>
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public interface PrintStreamWrapper extends Wrapper<PrintStream> {

    static PrintStream filter(
            final PrintStream printStream,
            final StringFilter filter) {
        return new StringFilterPrintStreamWrapper(printStream, filter);
    }

    static PrintStream filter(
            final PrintStream printStream,
            final ByteFilter filter) {
        return new ByteFilterPrintStreamWrapper(printStream, filter);
    }

    @SuppressWarnings("unchecked")
    static Optional<Wrapper<PrintStream>> innerWrapper(final PrintStream printStream) {
        if (printStream instanceof PrintStreamWrapper) {
            return ((Wrapper<PrintStream>) printStream).getInnerWrapper();
        }
        return Optional.empty();
    }

    /**
     * Creates a PrintStream that copies {@link PrintStream#write(int)} and
     * {@link PrintStream#write(byte[], int, int)} calls to both the left and the
     * right PrintStreams.
     *
     * @param left the first PrintStream
     * @param right the second PrintStream
     *
     * @return a PrintStream
     */
    static PrintStream copy(
            final PrintStream left,
            final PrintStream right
                           ) {
        return new CopyPrintStreamWrapper(left, right);
    }

    static PrintStream transform(
            final PrintStream original,
            final StringTransform transformer
                                ) {
        return new StringTransformPrintStreamWrapper(original, transformer);
    }

    static PrintStream transform(
            final PrintStream original,
            final ByteTransform transformer
                                ) {
        return new ByteTransformPrintStreamWrapper(original, transformer);
    }

    @Override
    default PrintStream getWrapperSubject() {
        return printStreamWrapperInner()
                .map(PrintStreamWrapper::printStreamDelegate)
                .orElseGet(this::getWrapperSubject);
    }

    /**
     * The content of the PrintStreamWrapper as a PrintStream.
     *
     * @return The content of the PrintStreamWrapper as a PrintStream
     */
    default PrintStream printStreamDelegate() {
        return getWrapperSubject();
    }

    /**
     * Finds the contained PrintStreamWrapper if present.
     *
     * @return an Optional containing the wrapper PrintStreamWrapper, or empty if there is no inner wrapper.
     */
    Optional<PrintStreamWrapper> printStreamWrapperInner();

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
     * @see PrintStream#print(char)
     * @see PrintStream#println(char)
     */
    void write(int b);

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
    void write(
            byte[] buf,
            int off,
            int len
    );

    interface StringFilter extends Predicate<String> {}
    interface ByteFilter extends Predicate<Byte> {}

    interface StringTransform extends Function<String, String>{}
    interface ByteTransform extends Function<Byte, Byte>{}
}
