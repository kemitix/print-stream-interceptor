package net.kemitix.wrapper.printstream;

import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Provides helper for {@link PrintStreamWrapper}s that processes the bytes writing to the
 * {@link PrintStreamWrapper#write(byte[], int, int)}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
interface BytePrintStreamWrapper {

    /**
     * Scan the buffer, from off to len, and give it to the byteConsumer.
     *
     * @param buf          the buffer to process
     * @param off          the offset within the buffer to begin
     * @param len          the number of bytes to process
     * @param byteConsumer the consumer to process each byte
     */
    static void forEachByteInBuffer(
            final byte[] buf,
            final int off,
            final int len,
            final Consumer<Byte> byteConsumer
                                   ) {
        if ((len < 0) || (buf.length < (off + len))) {
            throw new IndexOutOfBoundsException(
                    String.format("buf.length: %d, off: %d, len: %d", buf.length, off, len));
        }
        IntStream.range(off, off + len)
                .map(i -> buf[i])
                .forEach(b -> byteConsumer.accept((byte) b));
    }
}
