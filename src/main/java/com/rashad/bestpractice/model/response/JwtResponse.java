package com.rashad.bestpractice.model.response;

import com.rashad.bestpractice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private User user;

//    public JwtResponse(String accessToken, String refreshToken, User user) {
//        this.accessToken = accessToken;
//        this.refreshToken = refreshToken;
//        this.user = user;
//    }
}
