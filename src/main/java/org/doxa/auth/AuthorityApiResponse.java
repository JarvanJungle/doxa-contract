package org.doxa.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.doxa.auth.Authority;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AuthorityApiResponse {
    private String status;
    private List<Authority> data;
}
