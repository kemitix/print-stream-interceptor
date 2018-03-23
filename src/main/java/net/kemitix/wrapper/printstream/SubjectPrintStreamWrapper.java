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

import lombok.RequiredArgsConstructor;
import net.kemitix.wrapper.Wrapper;

import java.io.PrintStream;
import java.util.Optional;

/**
 * A PrintStreamWrapper that contains the PrintStream directly.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@RequiredArgsConstructor
class SubjectPrintStreamWrapper implements PrintStreamWrapper {

    private final PrintStream subject;

    @Override
    public Optional<PrintStreamWrapper> printStreamWrapperInner() {
        return Optional.empty();
    }

    @Override
    public void write(final int b) {
        subject.write(b);
    }

    @Override
    public void write(
            final byte[] buf,
            final int off,
            final int len
    ) {
        subject.write(buf, off, len);
    }

    @Override
    public PrintStream wrapperSubject() {
        return subject;
    }

    @Override
    public Optional<Wrapper<PrintStream>> wrapperInner() {
        return Optional.empty();
    }
}
