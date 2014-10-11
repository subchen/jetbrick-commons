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
package jetbrick.config;

import java.io.InputStream;
import java.util.Properties;
import javax.xml.stream.*;

final class XmlPropertiesLoader {

    public static Properties load(InputStream is, boolean withRootName) {
        Properties props = new Properties();
        if (is == null) {
            return props;
        }

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        try {
            reader = factory.createXMLStreamReader(is);
            StringBuilder sb = new StringBuilder();
            int level = 0;
            while (reader.hasNext()) {
                switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT: {
                    if (level > 0 || withRootName) {
                        String tag = reader.getLocalName();
                        String value = reader.getAttributeValue(null, "value");
                        if (sb.length() > 0) {
                            sb.append('.');
                        }
                        sb.append(tag);
                        if (value != null) {
                            props.put(sb.toString(), value);
                        }
                    }
                    level++;
                    break;
                }
                case XMLStreamConstants.END_ELEMENT: {
                    level--;
                    if (level > 0 || withRootName) {
                        int pos = sb.lastIndexOf(".");
                        sb.setLength(Math.max(0, pos));
                    }
                    break;
                }
                case XMLStreamConstants.CHARACTERS:
                case XMLStreamConstants.CDATA: {
                    if (level > 0 || withRootName) {
                        String value = reader.getText();
                        value = value.trim();
                        if (value.length() > 0) {
                            props.put(sb.toString(), value);
                        }
                        break;
                    }
                }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return props;
    }
}
