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
package jetbrick.bean;

public final class Filters {

    public static interface FieldFilter {
        public boolean accept(FieldInfo field);
    }

    public static interface MethodFilter {
        public boolean accept(MethodInfo method);
    }

    public static final FieldFilter PUBLIC_FIELD = new FieldFilter() {
        @Override
        public boolean accept(FieldInfo field) {
            return field.isPublic();
        }
    };

    public static final FieldFilter STATIC_FIELD = new FieldFilter() {
        @Override
        public boolean accept(FieldInfo field) {
            return field.isStatic();
        }
    };

    public static final FieldFilter INSTANCE_FIELD = new FieldFilter() {
        @Override
        public boolean accept(FieldInfo field) {
            return !field.isStatic();
        }
    };

    public static final FieldFilter PUBLIC_INSTANCE_FIELD = new FieldFilter() {
        @Override
        public boolean accept(FieldInfo field) {
            return field.isPublic() && !field.isStatic();
        }
    };

    public static final FieldFilter PUBLIC_STATIC_FINAL_FIELD = new FieldFilter() {
        @Override
        public boolean accept(FieldInfo field) {
            return field.isPublic() && field.isStatic() && field.isFinal();
        }
    };

    public static final MethodFilter PUBLIC_METHOD = new MethodFilter() {
        @Override
        public boolean accept(MethodInfo method) {
            return method.isPublic();
        }
    };

    public static final MethodFilter STATIC_METHOD = new MethodFilter() {
        @Override
        public boolean accept(MethodInfo method) {
            return method.isStatic();
        }
    };

    public static final MethodFilter INSTANCE_METHOD = new MethodFilter() {
        @Override
        public boolean accept(MethodInfo method) {
            return !method.isStatic();
        }
    };

    public static final MethodFilter PUBLIC_INSTANCE_METHOD = new MethodFilter() {
        @Override
        public boolean accept(MethodInfo method) {
            return method.isPublic() && !method.isStatic();
        }
    };

}
