package com.born.common.exception;

import com.born.common.Status;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-10-08 09:36:17
 */
public class GlobalException extends RuntimeException {

    private static final  long serialVersionUID=1L;

    private Status status;

    public GlobalException(Status status){
        super(status.toString());
        this.status=status;
    }

    public Status getStatus() {
        return status;
    }
}