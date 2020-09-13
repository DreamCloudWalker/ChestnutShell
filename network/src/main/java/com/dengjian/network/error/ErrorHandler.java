package com.dengjian.network.error;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import javax.net.ssl.SSLException;

import retrofit2.HttpException;

public class ErrorHandler {
    public static class ERROR {
        public static final int UNKNOWN = 1000;
        public static final int PARSE_ERROR = 1001;
        public static final int NETWORK_ERROR = 1002;
        public static final int HTTP_ERROR = 1003;
        public static final int SSL_ERROR = 1004;
        public static final int TIMEOUT_ERROR = 1005;
    }

    public static class ResponseThrowable extends Exception {
        public int code;
        public String message;

        public ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }
    }

    public static class ServerThrowable extends Exception {
        public int code;
        public String message;

        public ServerThrowable(String message, int code) {
            this.message = message;
            this.code = code;
        }
    }

    public static ResponseThrowable handleException(Throwable throwable) {
        ResponseThrowable exception;
        if (throwable instanceof HttpException) {
            exception = new ResponseThrowable(throwable, ERROR.HTTP_ERROR);
            exception.message = "网络错误";
            return exception;
        } else if (throwable instanceof ServerThrowable) {
            ServerThrowable serverThrowable = (ServerThrowable) throwable;
            exception = new ResponseThrowable(serverThrowable, serverThrowable.code);
            exception.message = serverThrowable.message;
            return exception;
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            exception = new ResponseThrowable(throwable, ERROR.PARSE_ERROR);
            exception.message = "解析错误";
            return exception;
        } else if (throwable instanceof ConnectException) {
            exception = new ResponseThrowable(throwable, ERROR.NETWORK_ERROR);
            exception.message = "连接失败";
            return exception;
        } else if (throwable instanceof SSLException) {
            exception = new ResponseThrowable(throwable, ERROR.SSL_ERROR);
            exception.message = "证书校验失败";
            return exception;
        } else if (throwable instanceof ConnectTimeoutException) {
            exception = new ResponseThrowable(throwable, ERROR.TIMEOUT_ERROR);
            exception.message = "连接超时";
            return exception;
        } else if (throwable instanceof SocketTimeoutException) {
            exception = new ResponseThrowable(throwable, ERROR.TIMEOUT_ERROR);
            exception.message = "连接超时";
            return exception;
        } else {
            exception = new ResponseThrowable(throwable, ERROR.UNKNOWN);
            exception.message = "未知错误";
            return exception;
        }
    }
}
