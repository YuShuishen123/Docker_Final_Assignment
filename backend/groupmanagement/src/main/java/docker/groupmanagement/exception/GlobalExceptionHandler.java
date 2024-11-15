package docker.groupmanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;  // 数据库异常
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import docker.groupmanagement.util.Result;

import java.sql.SQLException;

@RestControllerAdvice  // 统一处理 Controller 层的异常
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 处理自定义异常
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Result> handleCustomException(CustomException ex) {
        logger.error("CustomException: {}", ex.getMessage(), ex);
        Result result = new Result();
        result.setMsg(ex.getMessage());  // 错误信息
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // 处理空指针异常
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Result> handleNullPointerException(NullPointerException ex) {
        logger.error("NullPointerException: {}", ex.getMessage(), ex);
        Result result = new Result();
        result.setMsg("请求中缺少必要的参数");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // 处理非法参数异常
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("IllegalArgumentException: {}", ex.getMessage(), ex);
        Result result = new Result();
        result.setMsg("非法参数");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // 处理数据库异常（如：数据库连接失败，SQL语法错误等）
    @ExceptionHandler({DataAccessException.class, SQLException.class})
    public ResponseEntity<Result> handleDatabaseException(Exception ex) {
        logger.error("Database error: {}", ex.getMessage(), ex);
        Result result = new Result();
        result.setMsg("数据库操作异常，请稍后重试");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 统一处理其他所有未被捕获的异常（服务器级错误）
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleGeneralException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        Result result = new Result();
        result.setMsg("服务器发生错误，请稍后再试");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
