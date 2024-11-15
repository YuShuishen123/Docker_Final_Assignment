package docker.groupmanagement.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Result {
    private String msg;    // 响应信息
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;   // 存储其他信息（如JWT令牌）
    @JsonInclude(JsonInclude.Include.NON_NULL)  // 如果 token 为 null，则不返回
    private String token;  // 存储JWT令牌，若单独返回令牌可以添加此字段
}
