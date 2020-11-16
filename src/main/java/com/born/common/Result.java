package com.born.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-09-30 13:16:28
 */
@Data
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public Result(){
    }

    public Result(int code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public Result(Status status) {
        this.code=status.getCode();
        this.msg=status.getMsg();
    }

    public Result(Status status,Object data) {
        this.code=status.getCode();
        this.msg=status.getMsg();
        this.data = data;
    }

    public static Result success(int code, String msg, Object data) {
        return new Result(code,msg,data);
    }

    public static Result success(String msg, Object data) {
        return success(200, msg, data);
    }

    public static Result success(Object data) {
        return success("操作成功", data);
    }

    public static Result success() {
        return success(null);
    }



    public static Result fail(int code, String msg, Object data) {
        return new Result(code,msg,data);
    }

    public static Result fail(String msg, Object data) {
        return fail(400,msg,data);
    }

    public static Result fail(Object data) {
        return fail("操作失败", data);
    }

    public static Result fail() {
        return fail(null);
    }


}
