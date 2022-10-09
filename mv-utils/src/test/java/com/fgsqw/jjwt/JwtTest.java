package com.fgsqw.jjwt;


import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class JwtTest {

    @Test
    public void jwtTest() {
        JwtBuilder jwtBuilder = Jwts.builder();
        int time = 60 * 1000 * 60 * 24;
        String signature = "admin";
        String jwtToken = jwtBuilder
                // header
                .setHeaderParam("type","jwt")
                .setHeaderParam("alg","H256")
                // payload
                .claim("username","admin")
                .claim("role","admin")
                // 有效时间
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setId(UUID.randomUUID().toString())
                // signature 签名
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();

        System.out.println("jwtToken = " + jwtToken);
        System.out.println("----------------------------------------");

        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        System.out.println("claims.getExpiration() = " + claims.getExpiration());
        System.out.println("claims = " + claims);

    }
}
