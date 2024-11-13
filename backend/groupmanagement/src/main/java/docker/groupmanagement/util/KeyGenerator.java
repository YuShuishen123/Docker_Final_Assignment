package docker.groupmanagement.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class KeyGenerator {

    public static void main(String[] args) {
        // 生成 HS256 算法的密钥
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // 将密钥编码为 Base64 字符串
        String base64Key = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("密钥（Base64 编码）： " + base64Key);
    }
}
