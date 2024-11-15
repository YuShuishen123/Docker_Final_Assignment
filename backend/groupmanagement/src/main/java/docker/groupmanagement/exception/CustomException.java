package docker.groupmanagement.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    // 获取错误码
    private String errorCode;

    // 构造函数，只有错误信息
    public CustomException(String message) {
        super(message);
    }
    // 构造函数，错误信息和错误码
    public CustomException(String message, String errorCode) {
        super(message);  // 调用父类构造器
        this.errorCode = errorCode;
    }
}
