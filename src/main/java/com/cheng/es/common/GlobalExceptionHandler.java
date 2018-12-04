package com.cheng.es.common;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author fengcheng
 * @version 2017/4/7
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({RuntimeException.class, BindException.class})
    public Result exceptionHandler(Exception e) {
        e.printStackTrace();
        Result result;
        if (e instanceof BindException) {
            result = Result.of(ResultCode.FAILD_PARAM);
            List<FieldError> fieldErrors = ((BindException) e).getBindingResult().getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError fieldError : fieldErrors) {
                errorMessage.append(fieldError.getField()).append(":");
                errorMessage.append(fieldError.getDefaultMessage()).append(",");
            }
            result.setData(errorMessage);
            return result;
        } else {
            result = Result.of(ResultCode.FAILD);
        }
        result.setData(e.getMessage());
        return result;
    }

}
