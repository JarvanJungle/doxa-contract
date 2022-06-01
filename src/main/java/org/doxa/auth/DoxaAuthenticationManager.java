package org.doxa.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.doxa.auth.Authority;
import org.doxa.contract.exceptions.ObjectDoesNotExistException;
import org.doxa.contract.microservices.DTO.oauth.IOauthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoxaAuthenticationManager {
    /**
     * Injected services
     */
    private final IOauthService iOauthService;

    /**DoxaAuthenticationManager
     * Endpoints
     */

    private static final String CIRCUIT_BREAKER_IDENTIFIER = "oauth-circuit-breaker";

    private Map<String, Object> claims;


    public Long getCurrentUserId() {
        return (Long) getUserByKey("user_id");
    }

    public Object getUserByKey(String key) {
        initClaims();
        return claims.get(key);
    }


    private JSONObject findCompanyByUuid(String compUuid) {
        JSONArray companies = getCompanies();
        for (Object company : companies) {
            JSONObject object = (JSONObject) company;
            String uuid = (String) object.get("uuid");
            if (uuid.equals(compUuid)) {
                return object;
            }
        }
        return null;
    }

    public boolean isDoxaAdmin() {
        initClaims();
        String roles = (String) claims.get("roles");
        return roles.contains("DOXA_ADMIN");
    }

    private JSONArray getCompanies() {
        return (JSONArray) getUserByKey("companies");
    }

    public boolean hasAuthority(String compUuid, String authority) {
        try {
            JSONObject company = findCompanyByUuid(compUuid);
            if (company == null) {
                return false;
            }
            String userUuid = (String) claims.get("sub");
            List<Authority> authorities = iOauthService.getAuthorities(compUuid, userUuid);
            return userHasPermission(authorities, authority);
        } catch (ObjectDoesNotExistException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public boolean hasAuthority(String compUuid, String... authorities) {
        for (String authority : authorities) {
            if (hasAuthority(compUuid, authority)) {
                return true;
            }
        }
        return false;
    }

    private boolean userHasPermission(List<Authority> authorities, String authority) {
        String[] spliced = authority.split(":");
        String featureName = spliced[0];
        String action = spliced[1];
        List<Authority> authorityList = authorities.stream().filter(e -> e.getFeatureCode().equalsIgnoreCase(featureName)).collect(Collectors.toList());
        for (Authority feat : authorityList) {
            if ("all".equalsIgnoreCase(action)) {
                return true;
            }
            if ("read".equalsIgnoreCase(action) && feat.getActions().isRead()) {
                return feat.getActions().isRead();
            }
            if ("write".equalsIgnoreCase(action) && feat.getActions().isWrite()) {
                return feat.getActions().isWrite();
            }
            if ("approve".equalsIgnoreCase(action) && feat.getActions().isApprove()) {
                return feat.getActions().isApprove();
            }
        }
        return false;
    }

    public boolean hasAdminRole(String compUuid) {
        JSONObject company = findCompanyByUuid(compUuid);
        if (company == null) {
            return false;
        }
        String roles = (String) company.get("roles");

        return roles.contains("ENTITY_ADMIN") || roles.contains("COMPANY_ADMIN");
    }

    private void initClaims() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt principal = (Jwt) authentication.getPrincipal();
        claims = principal.getClaims();
    }
}
