package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;

import java.io.PrintStream;
import java.util.Optional;

class NestedPrintStreamWrapper implements PrintStreamWrapper {

    private final PrintStreamWrapper wrapper;

    NestedPrintStreamWrapper(final PrintStreamWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public Optional<PrintStreamWrapper> getInnerPrintStream() {
        return Optional.of(wrapper);
    }

    @Override
    public void write(final int b) {
        wrapper.write(b);
    }

    @Override
    public void write(
            final byte[] buf,
            final int off,
            final int len
                     ) {
        wrapper.write(buf, off, len);
    }

    @Override
    public PrintStream wrapperSubject() {
        return wrapper.wrapperSubject();
    }

    @Override
    public Optional<Wrapper<PrintStream>> wrapperInner() {
        return Optional.of(wrapper);
    }
}
