/**
 * Copyright 2013-2019 Guoqiang Chen, Shanghai, China. All rights reserved.
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
package jetbrick.web.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import javax.servlet.ServletContext;

public final class ServletUtils {

    public static File getWebroot(ServletContext sc) {
        String dir = sc.getRealPath("/");
        if (dir == null) {
            try {
                URL url = sc.getResource("/");
                if (url != null && "file".equals(url.getProtocol())) {
                    dir = URLDecoder.decode(url.getFile(), "utf-8");
                } else {
                    throw new IllegalStateException("Can't get webroot dir, url = " + url);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return new File(dir);
    }
}
