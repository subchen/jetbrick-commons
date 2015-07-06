package jetbrick.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AppListener {

    Class<? extends AppEvent> type() default AppEvent.NULL.class;

    boolean async() default true;

    int order() default 100;

}
