/**
 * Copyright 2013-2023 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
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
package jetbrick.collection.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Guoqiang Chen
 */
public class LoopIterator implements Iterator<Integer> {
    private final int start;
    private final int step;
    private final int stop;
    private int current;

    public LoopIterator(int start, int stop) {
        this(start, stop, 1);
    }

    public LoopIterator(int start, int stop, int step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.current = start;
    }

    @Override
    public boolean hasNext() {
        if (step > 0) {
            return current <= stop;
        } else {
            return current >= stop;
        }
    }

    @Override
    public Integer next() {
        if (hasNext()) {
            int next = current;
            current += step;
            return next;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return (stop - start + step) / step;
    }
}
