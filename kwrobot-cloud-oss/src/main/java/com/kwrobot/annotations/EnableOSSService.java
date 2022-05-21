package com.kwrobot.annotations;

import com.kwrobot.config.OSSServiceImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(OSSServiceImportSelector.class)
public @interface EnableOSSService {
}
