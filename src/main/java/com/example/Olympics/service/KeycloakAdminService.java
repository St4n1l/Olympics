package com.example.Olympics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminService {

    @Value("${keycloak.admin.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    private String getAdminToken() {
        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        assert response.getBody() != null;
        return (String) response.getBody().get("access_token");
    }

    public void createUser(String username, String email, String password) {
        String token = getAdminToken();
        String usersUrl = serverUrl + "/admin/realms/" + realm + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("enabled", true);
        user.put("credentials", List.of(Map.of(
                "type", "password",
                "value", password,
                "temporary", false
        )));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(usersUrl, request, Void.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                String userId = extractUserId(Objects.requireNonNull(response.getHeaders().getLocation()));
                assignAthleteRole(token, userId);
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new RuntimeException("Username or email already exists");
            }
            throw e;
        }
    }

    private String extractUserId(java.net.URI location) {
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private void assignAthleteRole(String token, String userId) {
        // First get the role representation
        String roleUrl = serverUrl + "/admin/realms/" + realm + "/roles/ATHLETE";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Map> roleResponse = restTemplate.exchange(
                roleUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

        Map<String, Object> role = roleResponse.getBody();

        // Then assign it to the user
        String assignUrl = serverUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.postForEntity(assignUrl, new HttpEntity<>(List.of(role), headers), Void.class);
        log.info("Assigned ATHLETE role to user {}", userId);
    }
}