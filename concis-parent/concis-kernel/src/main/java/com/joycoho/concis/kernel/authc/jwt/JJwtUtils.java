package com.joycoho.concis.kernel.authc.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JJwtUtils {
    /**
     * token 过期时间, 单位: 秒. 这个值表示 7 天
     */
    private static final long TOKEN_EXPIRED_TIME = 7 * 24 * 60 * 60;

    public static final String jwtId = "tokenId";

    /**
     * jwt 加密解密密钥(可自行填写)
     */
    private static final String JWT_SECRET = "LessIsMore";

    /**
     * 创建JWT TOKEN
     * @param claims
     * @param time
     * @return
     */
    public static String createJwtToken(Map<String, Object> claims, Long time) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        Date now = new Date(nowMillis);
        SecretKey secretKey = generatelKey();

        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(jwtId)               //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           //iat: jwt的签发时间
                .signWith(signatureAlgorithm, secretKey);//设置签名使用的签名算法和签名使用的秘钥
        if (time >= 0) {
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);     //设置过期时间
        }
        return builder.compact();
    }

    /**
     * 验证jwt
     */
    public static Claims verifyJwt(String token) {
        //签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generatelKey();
        Claims claims;
        try {
            claims = Jwts.parser()      //得到DefaultJwtParser
                    .setSigningKey(key) //设置签名的秘钥
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }//设置需要解析的jwt
        return claims;
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generatelKey() {
        String stringKey = JWT_SECRET;
        byte[] encodedKey = Base64.getDecoder().decode(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token) {
        String username = null;
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            username = claims.getSubject();
        }

        return username;
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

//    /**
//     * 验证token是否还有效
//     *
//     * @param token       客户端传入的token
//     * @param username 从数据库中查询出来的用户信息
//     */
//    public boolean validateToken(String token, String username) {
//        String u = getUserNameFromToken(token);
//        return u.equals(username) && !isTokenExpired(token);
//    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * demo:根据userId和openid生成token
     */
    public static String generateToken(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        //map.put("sub", openId);
        return createJwtToken(map, TOKEN_EXPIRED_TIME);
    }

    public static void main(String[] args) {
        // 生成token
        String s = generateToken("111", "20");
        System.out.println(s);

        // 验证
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMTEiLCJvcGVuSWQiOiIxMTEiLCJleHAiOjE1OTI1NTc3ODMsInVzZXJJZCI6MjAsImlhdCI6MTU5MjU1NTE5MSwianRpIjoidG9rZW5JZCJ9.X7JHnx3Y5wtb-n3pT9tft2I4hENJdeRxW2QWaI4jv2E";
        Claims claims = verifyJwt(token);
        String subject = claims.getSubject();
        String userId = (String)claims.get("userId");
        String openId = (String)claims.get("openId");
        String sub = (String)claims.get("sub");
        System.out.println("subject:" + subject);
        System.out.println("userId:" + userId);
        System.out.println("openId:" + openId);
        System.out.println("sub:" + sub);
    }
}
