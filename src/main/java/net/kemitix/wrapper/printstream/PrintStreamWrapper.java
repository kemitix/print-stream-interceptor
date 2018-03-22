package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;

import java.io.PrintStream;
import java.util.Optional;

public interface PrintStreamWrapper extends Wrapper<PrintStream> {

    static PrintStreamWrapper wrap(final PrintStream subject) {
        return new SubjectPrintStreamWrapper(subject);
    }

    static PrintStreamWrapper wrap(final PrintStreamWrapper wrapper) {
        return new NestedPrintStreamWrapper(wrapper);
    }

    default PrintStream getPrintStreamDelegate() {
        return getInnerPrintStream()
                .map(PrintStreamWrapper::getPrintStreamDelegate)
                .orElseGet(this::wrapperSubject);
    }

    Optional<PrintStreamWrapper> getInnerPrintStream();

    void write(int b);

    void write(
            byte[] buf,
            int off,
            int len
              );
}
