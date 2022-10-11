package com.fgsqw;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String KEY_USERNAME = "sub";
    private static final String KEY_CREATED = "created";
    // 密钥
    @Value("${jwt.secret}")
    private String secret;
    // 过期时间
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据 UserDetails 登录用户生成Token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(KEY_USERNAME,userDetails.getUsername());
        hashMap.put(KEY_CREATED,new Date());
        return generateToken(hashMap);
    }

    /**
     * 判断token是否可以被刷新
     * @param token
     * @return
     */
    public Boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = getClaimsByToken(token);
        claims.put(KEY_CREATED,new Date());
        return generateToken(claims);
    }

    /**
     * 从token中获取用户名
     * @param token
     * @return
     */
    public String getUserNameByToken(String token){
        String username;
        try{
            Claims claims = getClaimsByToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            username = null;
        }
        return username;
    }

    /**
     * 验证token 是否有效
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean verdictToken(String token,UserDetails userDetails){
        String username = getUserNameByToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断 token 是否失效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expired = getExpiredDataByToken(token);
        return expired.before(new Date());
    }

    /**
     * 从荷载中获取过期时间
     * @param token
     * @return
     */
    private Date getExpiredDataByToken(String token) {
        Claims claims = getClaimsByToken(token);
        return claims.getExpiration();
    }

    /**
     * 从token中获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsByToken(String token) {
        Claims claims = null;
        try{
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据荷载生成 JWT token
     * @param hashMap
     * @return
     */
    private String generateToken(Map<String, Object> hashMap){
        return Jwts.builder()
                .setClaims(hashMap)
                .setExpiration(generateExpiration())
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    /**
     * 获取过期时间
     * @return
     */
    private Date generateExpiration(){
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
}
