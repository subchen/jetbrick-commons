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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public final class RequestHeaderValuesMap extends StringEnumeratedMap<String[]> {
    private final HttpServletRequest request;

    public RequestHeaderValuesMap(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    protected Enumeration<String> getAttributeNames() {
        return request.getHeaderNames();
    }

    @Override
    protected String[] getAttribute(String key) {
        List<String> list = new ArrayList<String>();
        Enumeration<String> e = request.getHeaders(key);
        if (e != null) {
            while (e.hasMoreElements()) {
                list.add(e.nextElement());
            }
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    protected void setAttribute(String key, String[] value) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeAttribute(String key) {
        throw new UnsupportedOperationException();
    }
}
