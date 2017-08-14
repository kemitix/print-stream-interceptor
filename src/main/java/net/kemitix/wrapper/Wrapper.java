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

package net.kemitix.wrapper;

import java.util.Optional;

/**
 * Wrapper for generic types.
 *
 * @param <T> the type of object to wrap
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public interface Wrapper<T> {

    /**
     * Fetch the core item being Wrapper.
     *
     * @return the core item
     */
    T getCore();

    /**
     * Gets the inner wrapper if one is present.
     *
     * @return An Optional containing the inner wrapper if present, otherwise is empty
     */
    Optional<Wrapper<T>> findInnerWrapper();

    /**
     * Remove the wrapper from the chain of wrappers.
     *
     * <p>Can't be <em>this</em> wrapper.</p>
     *
     * @param wrapper the wrapper to remove
     */
    void remove(Wrapper<T> wrapper);

    /**
     * Provides the core item's own interface.
     *
     * @return the core item as a T object
     */
    T asCore();
}
