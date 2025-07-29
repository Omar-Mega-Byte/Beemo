package com.order.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {
    
    private final RestTemplate restTemplate;
    private final String userServiceUrl = "http://localhost:9001/";
    
    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Validate if user exists
     * 
     * @param userId The ID of the user to validate
     * @return true if user exists, false otherwise
     */
    public boolean validateUser(Long userId) {
        try {
            String url = userServiceUrl + "/users/" + userId + "/validate";
            Boolean isValid = restTemplate.getForObject(url, Boolean.class);
            return isValid != null && isValid;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with user-service: " + e.getMessage(), e);
        }
    }
}
