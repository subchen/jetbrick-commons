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
package jetbrick.web.servlet.map;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import jetbrick.util.concurrent.ConcurrentInitializer;
import jetbrick.util.concurrent.LazyInitializer;

public final class RequestCookieMap implements Map<String, Cookie> {
    private final HttpServletRequest request;

    private final ConcurrentInitializer<Map<String, Cookie>> map = new LazyInitializer<Map<String, Cookie>>() {
        @Override
        protected Map<String, Cookie> initialize() {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return Collections.emptyMap();
            } else {
                Map<String, Cookie> map = new HashMap<String, Cookie>();
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();
                    map.put(name, cookie);
                }
                return map;
            }
        }
    };

    public RequestCookieMap(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.get().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.get().containsValue(value);
    }

    @Override
    public Cookie get(Object key) {
        return map.get().get(key);
    }

    @Override
    public boolean isEmpty() {
        return map.get().isEmpty();
    }

    @Override
    public int size() {
        return map.get().size();
    }

    @Override
    public Set<Map.Entry<String, Cookie>> entrySet() {
        return map.get().entrySet();
    }

    @Override
    public Set<String> keySet() {
        return map.get().keySet();
    }

    @Override
    public Collection<Cookie> values() {
        return map.get().values();
    }

    @Override
    public synchronized Cookie put(String key, Cookie value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void putAll(Map<? extends String, ? extends Cookie> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Cookie remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void clear() {
        throw new UnsupportedOperationException();
    }

}
