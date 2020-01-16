package com.wxzd.gfzdj.global;

import java.util.List;

/**
 * Created by admin on 2018/1/23.
 * description: 错误信息
 */

public class ErrorListResponse {

    /**
     * messageList : [{"messageId":"MST0034","messageLevel":"ERROR","messageSubject":"手机号码已存在","messageBody":"手机号码已存在","messageForDeveloper":"MST0034","messageInstanceId":"6NXZ4X4FHBAWZLWEo4JSIJZV6Y","path":"/api/sms/17671790008/01","exceptionClass":"com.zdcx.base.common.exception.AppException"}]
     * status : 400
     */

    public int status;
    public List<ErrorBean> messageList;

    public static class ErrorBean {
        /**
         * messageId : MST0034
         * messageLevel : ERROR
         * messageSubject : 手机号码已存在
         * messageBody : 手机号码已存在
         * messageForDeveloper : MST0034
         * messageInstanceId : 6NXZ4X4FHBAWZLWEo4JSIJZV6Y
         * path : /api/sms/17671790008/01
         * exceptionClass : com.zdcx.base.common.exception.AppException
         */

        public String messageId;
        public String messageLevel;
        public String messageSubject;
        public String messageBody;
        public String messageForDeveloper;
        public String messageInstanceId;
        public String path;
        public String exceptionClass;
    }
}
