package net.kemitix.wrapper.printstream;

import net.kemitix.wrapper.Wrapper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PrintStreamWrapperTest {

    @Test
    public void canNotGetInnerWrapperWhenNotNested() {
        //given
        final PrintStreamWrapper wrapper = PrintStreamWrapper.wrap(printStream());
        //when
        final Optional<Wrapper<PrintStream>> result = wrapper.getInnerWrapper();
        //then
        assertThat(result).isEmpty();
    }

    private static PrintStream printStream() {
        return new PrintStream(new ByteArrayOutputStream());
    }

    @Test
    public void canGetInnerWrapperWhenNested() {
        //given
        final PrintStreamWrapper inner = PrintStreamWrapper.wrap(printStream());
        final PrintStreamWrapper wrapper = PrintStreamWrapper.wrap(inner);
        //when
        final Optional<Wrapper<PrintStream>> result = wrapper.getInnerWrapper();
        //then
        assertThat(result).contains(inner);
    }
}
