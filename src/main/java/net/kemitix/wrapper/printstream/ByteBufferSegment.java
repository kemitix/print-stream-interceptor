package net.kemitix.wrapper.printstream;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Represents a segment of a buffer of bytes.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@RequiredArgsConstructor
class ByteBufferSegment {

    private final byte[] buf;
    private final int off;
    private final int len;

    /**
     * Scan the buffer, from off to len, and give it to the byteConsumer.
     *
     * @param byteConsumer the consumer to process each byte
     */
    void forEach(final Consumer<Byte> byteConsumer) {
        if ((len < 0) || (buf.length < (off + len))) {
            throw new IndexOutOfBoundsException(
                    String.format("buf.length: %d, off: %d, len: %d", buf.length, off, len));
        }
        IntStream.range(off, off + len)
                .map(i -> buf[i])
                .forEach(b -> byteConsumer.accept((byte) b));
    }
}
