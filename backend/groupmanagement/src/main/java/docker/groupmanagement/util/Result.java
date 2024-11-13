package docker.groupmanagement.util;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Result {
    private int code;      // 状态码
    private String msg;    // 响应信息
    private Object data;   // 存储其他信息（如JWT令牌）
    private String token;  // 存储JWT令牌，若单独返回令牌可以添加此字段
}
