package com.ms.log.annotation;

import com.ms.log.enums.BusinessEnum;
import com.ms.log.enums.OperatorEnum;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.annotation.*;

@Target(value = {ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    public String title() default "";

    public OperatorEnum operator() default OperatorEnum.MANAGE;

    public BusinessEnum business() default BusinessEnum.OTHER;

    public boolean isSaveRequestData() default true;

    public boolean isSaveResponseData() default true;
}
