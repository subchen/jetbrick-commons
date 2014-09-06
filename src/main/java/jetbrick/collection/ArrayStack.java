/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 * Email: subchen@gmail.com
 * URL: http://subchen.github.io/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.collection;

import java.util.EmptyStackException;

public final class ArrayStack<T> {
    private static final int DEFAULT_CAPACITY = 16;
    private Object[] elements;
    private int size;

    public ArrayStack() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayStack(int initialCapacity) {
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    /**
     * Tests if this stack is empty.
     * @return true if and only if this stack contains no items; false otherwise.
     */
    public boolean empty() {
        return size == 0;
    }

    /**
     * Removes all of the elements from this stack.
     */
    public void clear() {
        int i = size;
        Object[] els = elements;
        while (i-- > 0) {
            els[i] = null;
        }
        this.size = 0;
    }

    /**
     * Returns the number of elements in this stack.
     */
    public int size() {
        return size;
    }

    /**
     * Pushes an item onto the top of this stack.
     *
     * @param element － the element to be pushed onto this stack
     * @return the item argument
     */
    public T push(T element) {
        int i;
        Object[] els;
        if ((i = size++) >= (els = elements).length) {
            System.arraycopy(els, 0, els = elements = new Object[i << 1], 0, i);
        }
        els[i] = element;
        return element;
    }

    /**
     * Removes the object at the top of this stack and returns that object as the value of this function.
     *
     * @return The object at the top of this stack
     * @throws EmptyStackException - if this queue is empty
     */
    @SuppressWarnings("unchecked")
    public T pop() throws EmptyStackException {
        int i;
        if ((i = --size) >= 0) {
            T element = (T) elements[i];
            elements[i] = null;
            return element;
        } else {
            size = 0;
            throw new EmptyStackException();
        }
    }

    /**
     * Looks at the object at the top of this stack without removing it from the stack.
     *
     * @return the object at the top of this stack
     * @throws EmptyStackException - if this stack is empty.
     */
    @SuppressWarnings("unchecked")
    public T peek() throws EmptyStackException {
        if (size == 0) throw new EmptyStackException();
        return (T) elements[size - 1];
    }

    @SuppressWarnings("unchecked")
    public T peek(int offset) throws IndexOutOfBoundsException {
        int index;
        if (offset >= 0 && (index = size - offset - 1) >= 0) {
            return (T) elements[index];
        } else {
            throw new IndexOutOfBoundsException("offset=" + offset);
        }
    }
}
