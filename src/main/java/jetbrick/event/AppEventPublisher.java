/**
 * Copyright 2013-2018 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jetbrick.bean.TypeResolverUtils;

public final class AppEventPublisher {
    private static final List<AppEventListenerAgent> listeners = new ArrayList<AppEventListenerAgent>();
    private static volatile ExecutorService executorService;

    public static void setExecutorService(ExecutorService executorService) {
        AppEventPublisher.executorService = executorService;
    }

    @SuppressWarnings("unchecked")
    public static void addEventListener(AppEventListener listener) {
        Class<?> cls = listener.getClass();
        AppListener annotation = cls.getAnnotation(AppListener.class);

        AppEventListenerAgent agent = new AppEventListenerAgent();
        agent.listener = listener;
        agent.type = annotation.type();
        agent.async = annotation.async();
        agent.order = annotation.order();

        if (agent.type == AppEvent.NULL.class) {
            agent.type = (Class<? extends AppEvent>) TypeResolverUtils.getRawType(AppEventListener.class.getTypeParameters()[0], cls);
        }

        listeners.add(agent);
        Collections.sort(listeners);
    }

    @SuppressWarnings("unchecked")
    public static void publishEvent(final AppEvent event) {
        // sync
        for (AppEventListenerAgent agent : listeners) {
            if (!agent.async && agent.type.isInstance(event)) {
                agent.listener.onAppEvent(event);
            }
        }

        // async
        ExecutorService executorService = getExecutorService();
        for (final AppEventListenerAgent agent : listeners) {
            if (agent.async && agent.type.isInstance(event)) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        agent.listener.onAppEvent(event);
                    }
                });
            }
        }
    }

    private static ExecutorService getExecutorService() {
        if (executorService == null) {
            synchronized (AppEventPublisher.class) {
                if (executorService == null) {
                    executorService = Executors.newFixedThreadPool(10);
                }
            }
        }
        return executorService;
    }

    static class AppEventListenerAgent implements Comparable<AppEventListenerAgent> {
        private AppEventListener listener;
        private Class<? extends AppEvent> type;
        private boolean async;
        private int order;

        @Override
        public int compareTo(AppEventListenerAgent o) {
            return this.order - o.order;
        }
    }
}
