package com.sp.framework.common.utils.jwt;

import com.sp.framework.common.utils.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtHelper {
    /**
     * 校验jwt
     * @param jsonWebToken
     * @param base64Security
     * @return
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security){
        try
        {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * 创建jwt对象
     * @param userName 用户名称
     * @param userId 用户ID
     * @param role 用户角色
     * @param audience 观众
     * @param issuer 发行者
     * @param TTLMillis 过期时间
     * @param base64Security base64 签名秘钥
     * @return
     */
    public static String createJWT(String userName, String userId, String role,
                                   String audience, String issuer, long TTLMillis, String base64Security)
    {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("role", role)
                .claim("username", userName)
                .claim("userid", userId)
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }

    /**
     * 创建jwt对象
     * @param clientDeviceType 终端类型
     * @param userName 用户名称
     * @param userId 用户ID
     * @param mobileNo 移动电话
     * @param email email
     * @param role 用户角色
     * @param audience 观众
     * @param issuer 发行者
     * @param TTLMillis 过期时间
     * @param base64Security base64 签名秘钥
     * @return
     */
    public static String createJWT(String clientDeviceType,String clientDeviceCode,String clientIpAddr,
                                   String userName, String userId,String mobileNo, String email, String role,
                                   String audience, String issuer, long TTLMillis, String base64Security)
    {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("role", role)
                .claim("clientDeviceType", !StringUtil.isBlank(clientDeviceType) ? clientDeviceType : "")
                .claim("clientDeviceCode", !StringUtil.isBlank(clientDeviceCode) ? clientDeviceCode : "")
                .claim("clientIpAddr", !StringUtil.isBlank(clientIpAddr) ? clientIpAddr : "")
                .claim("username", userName)
                .claim("userid", userId)
                .claim("mobileno", mobileNo)
                .claim("email", email)
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        return builder.compact();
    }

    /**
     *
     * 功能描述: appkey
     *
     * @param:
     * @return:
     * @auther: dell
     * @date: 2018/6/26 14:23
     */
    public static String createJWT(String tenantCode, String appId, String appSecret,
                                   String clientDeviceType,String clientDeviceCode,String clientIpAddr,
                                   String audience, String issuer, long TTLMillis,
                                   String base64Security) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("tenantCode", tenantCode)
                .claim("appId", appId)
                .claim("appSecret", appSecret)
                .claim("clientDeviceType", !StringUtil.isBlank(clientDeviceType) ? clientDeviceType : "")
                .claim("clientDeviceCode", !StringUtil.isBlank(clientDeviceCode) ? clientDeviceCode : "")
                .claim("clientIpAddr", !StringUtil.isBlank(clientIpAddr) ? clientIpAddr : "")
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);

        //添加Token过期时间
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }
}
