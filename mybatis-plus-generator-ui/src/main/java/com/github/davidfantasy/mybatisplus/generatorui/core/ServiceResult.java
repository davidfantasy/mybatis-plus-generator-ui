package com.github.davidfantasy.mybatisplus.generatorui.core;

import lombok.Data;

/**
 * @Title service层返回结果封装
 * @Author JXL
 * @Date 2019/3/26
 */
@Data
public class ServiceResult<T> {
    public static final String MSG_SUCCESS = "SUCCESS";

    public static final String MSG_FAILED = "FAILED";

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_ERROR = -1;

    private int code;

    private String msg;

    private T data;

    public boolean isSuccess() {
        return this.code == CODE_SUCCESS;
    }

    public static <T> ServiceResult<T> create() {
        ServiceResult<T> serviceResult = new ServiceResult<>();
        return serviceResult.code(CODE_SUCCESS).msg(MSG_SUCCESS);
    }

    public ServiceResult<T> code(int code) {
        this.code = code;
        return this;
    }

    public ServiceResult<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public ServiceResult<T> data(T data) {
        this.data = data;
        return this;
    }

    public static <T> ServiceResult<T> success() {
        ServiceResult<T> result = new ServiceResult<>();
        result.code = CODE_SUCCESS;
        result.msg = MSG_SUCCESS;
        return result;
    }

    public static <T> ServiceResult<T> success(T data) {
        ServiceResult<T> result = new ServiceResult<>();
        result.code = CODE_SUCCESS;
        result.msg = MSG_SUCCESS;
        result.data = data;
        return result;
    }

    public static <T> ServiceResult<T> failed(int code) {
        ServiceResult<T> result = new ServiceResult<>();
        result.code = code;
        result.msg = MSG_FAILED;
        return result;
    }

    public static <T> ServiceResult<T> failed() {
        ServiceResult<T> result = new ServiceResult<>();
        result.code = CODE_ERROR;
        result.msg = MSG_FAILED;
        return result;
    }

}
