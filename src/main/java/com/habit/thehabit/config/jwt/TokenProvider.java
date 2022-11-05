package com.habit.thehabit.config.jwt;

import com.habit.thehabit.member.command.app.dto.MemberDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.common.command.app.dto.TokenDTO;
import com.habit.thehabit.common.command.app.exception.TokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private final String AUTHORITIES_KEY;
    private final String BEARER_TYPE;
    private final UserDetailsService userDetailsService;
    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final Key key;

    public TokenProvider(@Value("${security.jwt.secret}") String secretKey, @Value("${security.jwt.token.expire-length}") long expireTime,
                         @Value("${security.jwt.authority-type") String authoritesKey, @Value("${security.jwt.bearer-type}") String bearerType,
                         UserDetailsService userDetailsService){

        this.userDetailsService = userDetailsService;

        /** secretkey를 활용하여 key 값 매핑 */
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        /** yml 파일로부터 상수 값 매핑*/
        this.ACCESS_TOKEN_EXPIRE_TIME = expireTime;
        this.AUTHORITIES_KEY = authoritesKey;
        this.BEARER_TYPE = bearerType;

    }

    public TokenDTO generateTokenDTO(Member member){

        /** 권한 가져오기 */
        String role = member.getMemberRole();

        /** Jwt 토큰의 payload부분에 담을 claim(정보의 한 조각) 설정 */
        Claims claims = Jwts
                .claims()
                .setSubject(member.getMemberId());
        claims.put(AUTHORITIES_KEY, role);


        /** Access Token 생성 */
        long curTime = (new Date()).getTime();
        Date accessTokenExpireTime = new Date( curTime + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpireTime)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return new TokenDTO(BEARER_TYPE, member.getName(), accessToken, accessTokenExpireTime.getTime());
    }

    public String getUserId(String accessToken){
        /** AccessToken로부터 id를 parse 해옴 */
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }

    public Authentication getAuthentication(String accessToken){

        /** calim 꺼내오기  */
        Claims claims = parseClaims(accessToken);

        /** token 안에 auth 정보가 있는지 체크*/
        if(claims.get(AUTHORITIES_KEY) == null){
            throw new RuntimeException("토큰 안에 권한 정보가 없습니다.");
        }

        /** claim으로부터 권한 정보 가져오기 */
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

        log.info("[TokenProvider] authorities : {}", authorities);

        /** UserDetails 생성 후 authentication token 리턴 */
        System.out.println("this.getUserId(accessToken) = " + this.getUserId(accessToken));
//        UserDetails userDetails = userDetailsService.loadUserByUsername( this.getUserId(accessToken) );
//        UserDetails userDetails = userDetailsService.loadUserByUsername( "test02" );
        UserDetails userDetails = userDetailsService.loadUserByUsername( this.getUserId(accessToken) );
        log.info("==========================================================");
        System.out.println("userDetails detect " + userDetails.getAuthorities());
        log.info("==========================================================");
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            throw new TokenException("잘못된 JWT 형식입니다.");
        } catch(ExpiredJwtException e){
            throw new TokenException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e){
            throw new TokenException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e){
            throw new TokenException("잘못된 토큰입니다.");
        }
    }

    private Claims parseClaims(String accessToken) {
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch(ExpiredJwtException e){
            return e.getClaims();
        }
    }

}
