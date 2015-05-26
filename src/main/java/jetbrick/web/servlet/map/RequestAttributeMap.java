/**
 * Copyright 2013-2015 Guoqiang Chen, Shanghai, China. All rights reserved.
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

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

public final class RequestAttributeMap extends StringEnumeratedMap<Object> {
    private final HttpServletRequest request;

    public RequestAttributeMap(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    protected Enumeration<String> getAttributeNames() {
        return request.getAttributeNames();
    }

    @Override
    protected Object getAttribute(String key) {
        return request.getAttribute(key);
    }

    @Override
    protected void setAttribute(String key, Object value) {
        request.setAttribute(key, value);
    }

    @Override
    protected void removeAttribute(String key) {
        request.removeAttribute(key);
    }
}
