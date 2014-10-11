/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.util.concurrent;

import java.util.concurrent.*;

public abstract class BackgroundInitializer<T> implements ConcurrentInitializer<T> {
    private ExecutorService externalExecutor;
    private ExecutorService executor;
    private Future<T> future;

    protected BackgroundInitializer() {
        this(null);
    }

    protected BackgroundInitializer(ExecutorService exec) {
        setExternalExecutor(exec);
    }

    public final synchronized ExecutorService getExternalExecutor() {
        return externalExecutor;
    }

    public synchronized boolean isStarted() {
        return future != null;
    }

    public final synchronized void setExternalExecutor(ExecutorService externalExecutor) {
        if (isStarted()) {
            throw new IllegalStateException("Cannot set ExecutorService after start()!");
        }
        this.externalExecutor = externalExecutor;
    }

    public synchronized boolean start() {
        if (!isStarted()) {
            executor = getExternalExecutor();
            ExecutorService tempExec;
            if (executor == null) {
                executor = (tempExec = createExecutor());
            } else {
                tempExec = null;
            }
            future = executor.submit(createTask(tempExec));

            return true;
        }
        return false;
    }

    @Override
    public T get() {
        try {
            return getFuture().get();
        } catch (ExecutionException e) {
            if (e.getCause() == null) {
                return null;
            }
            throw new IllegalStateException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    public synchronized Future<T> getFuture() {
        if (future == null) {
            throw new IllegalStateException("start() must be called first!");
        }
        return future;
    }

    protected final synchronized ExecutorService getActiveExecutor() {
        return executor;
    }

    protected int getTaskCount() {
        return 1;
    }

    protected abstract T initialize() throws Exception;

    private Callable<T> createTask(ExecutorService execDestroy) {
        return new InitializationTask(execDestroy);
    }

    private ExecutorService createExecutor() {
        return Executors.newFixedThreadPool(getTaskCount());
    }

    private class InitializationTask implements Callable<T> {
        private final ExecutorService execFinally;

        public InitializationTask(ExecutorService exec) {
            execFinally = exec;
        }

        @Override
        public T call() throws Exception {
            try {
                return initialize();
            } finally {
                if (execFinally != null) {
                    execFinally.shutdown();
                }
            }
        }
    }
}
