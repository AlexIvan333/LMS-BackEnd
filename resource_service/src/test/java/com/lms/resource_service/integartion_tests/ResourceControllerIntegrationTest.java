//package com.lms.resource_service.integartion_tests;
//
//import com.lms.resource_service.entites.ResourceEntity;
//import com.lms.resource_service.repositories.ResourceRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.*;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class ResourceControllerIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private ResourceRepository resourceRepository;
//
//    private String jwtToken;
//
//    @BeforeEach
//    void setUp() {
//        jwtToken = obtainAdminTokenFromAuthService() ;
//    }
//
//    @AfterEach
//    void tearDown() {
//        resourceRepository.deleteAll();
//        TestSecurityUtil.clearAuthentication();
//    }
//
//    private String obtainAdminTokenFromAuthService() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        String email = "admin@gmail.com";
//        String password = "password";
//        String checkUrl = "http://host.docker.internal:8091/auth/login/admin";
//        String createAdminUrl = "http://host.docker.internal:8091/auth/admins";
//
//        // Attempt to login first (if admin exists, this will succeed)
//        String loginRequestBody = """
//        {
//            "email": "admin@gmail.com",
//            "password": "password"
//        }
//    """;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> loginRequest = new HttpEntity<>(loginRequestBody, headers);
//
//        try {
//            ResponseEntity<String> loginResponse = restTemplate.postForEntity(checkUrl, loginRequest, String.class);
//            return loginResponse.getBody();  // Admin exists, return token
//        } catch (Exception ex) {
//            System.out.println("Admin does not exist yet, proceeding to create it.");
//        }
//
//        // Admin does not exist, create it
//        String createAdminRequestBody = """
//        {
//            "firstName": "Admin",
//            "lastName": "User",
//            "middleName": "Super",
//            "streetName": "Main Street",
//            "email": "admin@gmail.com",
//            "streetNumber": 1,
//            "country": "Netherlands",
//            "city": "Eindhoven",
//            "zipCode": "5600"
//        }
//    """;
//
//        HttpEntity<String> createAdminRequest = new HttpEntity<>(createAdminRequestBody, headers);
//
//        ResponseEntity<String> createResponse = restTemplate.postForEntity(createAdminUrl, createAdminRequest,String.class);
//        System.out.println("Admin created successfully: " + createResponse.getBody());
//
//        // Now login again after creation to get the token
//        ResponseEntity<String> loginResponse = restTemplate.postForEntity(checkUrl, loginRequest, String.class);
//        return loginResponse.getBody();
//    }
//
//
//    @Test
//    @DisplayName("Should upload a resource successfully")
//    void uploadResource_shouldSaveFile() {
//        // Prepare file
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.COOKIE, "token=" + jwtToken);
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("file", new ByteArrayResource("Hello LMS!".getBytes()) {
//            @Override
//            public String getFilename() {
//                return "testfile.txt";
//            }
//        });
//
//        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/resources", request, String.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertThat(resourceRepository.findAll()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("Should download the uploaded resource successfully")
//    void downloadResource_shouldReturnFileData() {
//        // Prepare and save resource manually
//        ResourceEntity resource = new ResourceEntity();
//        resource.setFileName("downloadTest.txt");
//        resource.setFileType(MediaType.TEXT_PLAIN_VALUE);
//        resource.setFileData("Download test content".getBytes());
//        ResourceEntity savedResource = resourceRepository.save(resource);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.COOKIE, "token=" + jwtToken);
//
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        ResponseEntity<byte[]> response = restTemplate.exchange(
//                "/resources/download/" + savedResource.getId(),
//                HttpMethod.GET,
//                requestEntity,
//                byte[].class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertThat(response.getBody()).isEqualTo("Download test content".getBytes());
//    }
//
//    @Test
//    @DisplayName("Should fail to download non-existing resource")
//    void downloadNonExistingResource() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.COOKIE, "token=" + jwtToken);
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        ResponseEntity<byte[]> response = restTemplate.exchange(
//                "/resources/download/999",
//                HttpMethod.GET,
//                requestEntity,
//                byte[].class
//        );
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Should validate resource IDs successfully")
//    void validateResourceIds() {
//        ResourceEntity resource = new ResourceEntity();
//        resource.setFileName("file.txt");
//        resource.setFileType("text/plain");
//        resource.setFileData("Sample content".getBytes());
//        ResourceEntity savedResource = resourceRepository.save(resource);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.COOKIE, "token=" + jwtToken);
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        ResponseEntity<Long[]> response = restTemplate.exchange(
//                "/resources/validate?ids=" + savedResource.getId(),
//                HttpMethod.GET,
//                requestEntity,
//                Long[].class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertThat(response.getBody()).contains(savedResource.getId());
//    }
//
//}
