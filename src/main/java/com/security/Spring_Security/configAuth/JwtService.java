package com.security.Spring_Security.configAuth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service

public class JwtService {

    private static final String SECRETE_KEY="allkeysgenerate.com/random/generates_security_keys.aspx";

    public String extractUsername(String token) {
        return extractClaim(Claims::getSubject,token);
    }
    public <T>T extractClaim(Function<Claims,T> func,String token){
        Claims claims = extractAllClaims(token);
        return func.apply(claims);
    }
    public String generateToken (UserDetails userDetails){
        return generateAllToken(new HashMap<>(),userDetails);
    }
    public String generateAllToken(Map<String,Object> func, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(func)
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()*600*24*60))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !iSTokenExpired(token);
    }
    public boolean iSTokenExpired(String token){
        return expiration(token).before(new Date());
    }
    public Date expiration(String token){
        return extractClaim(Claims::getExpiration,token);
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignInKey(){
        byte[] decodeKey = Decoders.BASE64.decode(SECRETE_KEY);
        return Keys.hmacShaKeyFor(decodeKey);
    }
}
