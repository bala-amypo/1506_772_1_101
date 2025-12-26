/*package com.example.demo.dto;

import lombok.*;

import java.util.Set;

@Getter @Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String email;
    private Set<String> roles;
}
*/
package com.example.demo.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    
    private String token;
    private Long userId;
    private String email;
    private Set<String> roles;
}