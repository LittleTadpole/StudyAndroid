package com.wxzd.gfzdj.global;

/**
 * 服务器返回的对象
 */
public class Response<T> {

    private int status;  //状态码  0：失败  1：成功
    private String msg; // 显示的信息
    private T data; // 业务数据

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg == null ? "后台提示消息为空" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResults() {
        return data;
    }

    public void setResults(T results) {
        this.data = results;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", results=" + data.toString() +
                '}';
    }
}
