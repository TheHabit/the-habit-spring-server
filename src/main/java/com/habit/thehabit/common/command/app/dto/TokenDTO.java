package com.habit.thehabit.common.command.app.dto;

public class TokenDTO {

    private String grantType;
    private String memberName;
    private String accessToken;
    private Long accessTokenExpireTime;


    public TokenDTO(String grantType, String memberName, String accessToken, long accessTokenExpireTime) {
        this.grantType = grantType;
        this.memberName = memberName;
        this.accessToken = accessToken;
        this.accessTokenExpireTime = accessTokenExpireTime;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getAccessTokenExpireTime() {
        return accessTokenExpireTime;
    }

    public void setAccessTokenExpireTime(Long accessTokenExpireTime) {
        this.accessTokenExpireTime = accessTokenExpireTime;
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "grantType='" + grantType + '\'' +
                ", memberName='" + memberName + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", accessTokenExpireTime=" + accessTokenExpireTime +
                '}';
    }
}
