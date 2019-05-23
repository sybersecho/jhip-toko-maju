package com.toko.maju.web.rest.response;

public class GeraiResponse {
    private Integer status;
    private String desc;

    public final static Integer SUCCESS_STATUS = 0;
    public final static Integer FAIL_STATUS = 1;
    public final static Integer ERROR_STATUS = 2;

    private final static String SUCCESS_RESP_DESC = "success";
    private final static String FAIL_RESP_DESC = "fail";
    private final static String ERROR_RESP_DESC = "error";

    public GeraiResponse() {
    }

    public GeraiResponse(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static GeraiResponse SuccessResponse(){
        return new GeraiResponse(SUCCESS_STATUS, SUCCESS_RESP_DESC);
    }

    public static GeraiResponse FailResponse(){
        return new GeraiResponse(FAIL_STATUS, FAIL_RESP_DESC);
    }

    public static GeraiResponse ErrorResponse(){
        return new GeraiResponse(ERROR_STATUS, ERROR_RESP_DESC);
    }

    @Override
    public String toString() {
        return "GeraiResponse{" +
            "status=" + status +
            ", desc='" + desc + '\'' +
            '}';
    }
}
