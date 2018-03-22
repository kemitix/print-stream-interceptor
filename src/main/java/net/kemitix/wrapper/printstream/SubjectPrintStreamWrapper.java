package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;

import java.io.PrintStream;
import java.util.Optional;

class SubjectPrintStreamWrapper implements PrintStreamWrapper {

    private final PrintStream subject;

    SubjectPrintStreamWrapper(final PrintStream subject) {
        this.subject = subject;
    }

    @Override
    public Optional<PrintStreamWrapper> getInnerPrintStream() {
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
