package com.fgsqw;


import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class JwtUtil {

    static int time = 1000 * 60 * 60 * 24;

    public static String CreJwtToken(){
        JwtBuilder jwtBuilder = Jwts.builder();
        return  jwtBuilder
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .compact();
    }
}
