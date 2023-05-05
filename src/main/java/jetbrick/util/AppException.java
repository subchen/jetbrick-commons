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
package jetbrick.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String lineSeparator = System.getProperty("line.separator");

    private Object[] args;
    private Map<String, Object> props;

    public static AppException unchecked(Throwable e) {
        if (e instanceof AppException) {
            return (AppException) e;
        } else {
            return new AppException(e);
        }
    }

    public AppException() {
        super();
    }

    public AppException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(Throwable cause, String message, Object... args) {
        super(message, cause);
        this.args = args;
    }

    public Map<String, Object> getProperties() {
        if (props == null) {
            return Collections.<String, Object> emptyMap();
        } else {
            return Collections.unmodifiableMap(props);
        }
    }

    public Object get(String name) {
        return props == null ? null : props.get(name);
    }

    public AppException set(String name, Object value) {
        if (props == null) {
            props = new LinkedHashMap<String, Object>();
        }
        props.put(name, value);
        return this;
    }

    public String getSimpleMessage() {
        String message = super.getMessage();
        if (message != null) {
            if (args != null && args.length > 0) {
                message = String.format(message, args);
            }
        }
        return message;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        String message = getSimpleMessage();
        if (message != null) {
            sb.append(message);
        }
        if (props != null) {
            for (String key : props.keySet()) {
                if (sb.length() > 0) {
                    sb.append(lineSeparator);
                    sb.append('\t');
                }
                Object value = props.get(key);
                if (value != null && value.getClass().isArray()) {
                    value = ArrayUtils.toString(value);
                }
                sb.append(key);
                sb.append(" = [");
                sb.append(value);
                sb.append(']');
            }
        }
        return sb.length() == 0 ? null : sb.toString();
    }
}
