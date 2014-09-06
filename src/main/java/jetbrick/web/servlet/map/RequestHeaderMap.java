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
package jetbrick.web.servlet.map;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

public class RequestHeaderMap extends StringEnumeratedMap<String> {
    private final HttpServletRequest request;

    public RequestHeaderMap(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    protected Enumeration<String> getAttributeNames() {
        return request.getHeaderNames();
    }

    @Override
    protected String getAttribute(String key) {
        return request.getHeader(key);
    }

    @Override
    protected void setAttribute(String key, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeAttribute(String key) {
        throw new UnsupportedOperationException();
    }
}
