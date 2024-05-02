package com.material.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;

import java.util.Map;

public class JwtUtil {

    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        //指定加密算法
        SecureDigestAlgorithm<SecretKey, SecretKey> algorithm = Jwts.SIG.HS256;
        //生成JWT的时间
        long expMillis = System.currentTimeMillis()+ttlMillis;
        Date exp = new Date(expMillis);
        //密钥实例
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String compact = Jwts.builder()
                .signWith(key, algorithm) //设置签名使用的签名算法和签名使用的秘钥
                //如果有私有声明，一点要先设置这个自己创建的私有的声明，这个是给builder的claims赋值，一旦卸载标准的声明赋值之后，就是覆盖了那些标准的声明的
                .expiration(exp)
                .claims(claims) //设置自定义负载信息
                .compact();//设置过期时间
        return compact;

    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    public static Jws<Claims> parseJWT(String token, String secretKey){
        //密钥实例
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Jws<Claims> claimsJws = Jwts.parser()
                // 设置签名的密钥
                .verifyWith(key)
                .build()
                // 设置要解析的jwt
                .parseSignedClaims(token);


        return claimsJws;
    }


}
