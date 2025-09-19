package com.udd.PropertyTax;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthRequestService {

    private static final String AUTH_URL = "https://nagarsewa.uk.gov.in/user/oauth/token";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAuthToken() throws Exception {

        // Set Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic ZWdvdi11c2VyLWNsaWVudDo=");

        // Set Form Parameters
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", "CounterEmployee-testing");
        body.add("password", "eGov@123");
        body.add("grant_type", "password");
        body.add("scope", "read");
        body.add("tenantId", "uk.testing");
        body.add("userType", "EMPLOYEE");

        // Prepare request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Call API
        ResponseEntity<String> response = restTemplate.postForEntity(AUTH_URL, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());

            // Typically, the token is in access_token field. tenantId might also be returned.
            String accessToken = root.path("access_token").asText();
         
            System.out.println("Access Token: " + accessToken);
            

            return accessToken; // return what you need
        } else {
            throw new RuntimeException("Failed to fetch token: " + response.getStatusCode());
        }
    }
    
    
    public PropertyResponse fetchProperty(String tenantId, String propertyId, String accessToken)  throws JsonMappingException, JsonProcessingException {
		
		 String BASE_URL =
	            "https://nagarsewa.uk.gov.in/property-services/property/_search";
		 
		 
		 String url = BASE_URL + "?tenantId=" + tenantId + "&propertyIds=" + propertyId;
	       

		//String url = "https://nagarsewa.uk.gov.in/property-services/property/_search?tenantId=uk.testing&propertyIds=PT-248499-000201";

		RestTemplate restTemplate = new RestTemplate();

		// JSON body
		String requestJson = "{\n" + "  \"RequestInfo\": {\n" + "    \"apiId\": \"Mihy\",\n" + "    \"ver\": \".01\",\n"
				+ "    \"action\": \"\",\n" + "    \"did\": \"1\",\n" + "    \"key\": \"\",\n"
				+ "    \"msgId\": \"20170310130900|en_IN\",\n" + "    \"requesterId\": \"\",\n"
				+ "    \"authToken\": \"820af224-131a-46aa-a22e-d672a2510f6f\"\n" + "  }\n" + "}";

		// Set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

		// Make POST request
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		
		PropertyResponse dto = new PropertyResponse();

		if (response.getStatusCode() == HttpStatus.OK) {
			String responseBody = response.getBody();

			// Parse JSON to get propertyType
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(responseBody);

			
		 

			JsonNode property = root.path("Properties").get(0);

			if (!property.isMissingNode()) {
				JsonNode address = property.path("address");
				JsonNode locality = address.path("locality");

				// Map property fields
				dto.setProperty_type(property.path("propertyType").asText("N/A"));
				dto.setProperty_id(property.path("propertyId").asText("N/A"));
				dto.setPlot_area(property.path("landArea").asDouble(0.0));
				dto.setTotal_no_of_floors(property.path("noOfFloors").asInt(0));
				dto.setSuper_builtup_area(property.path("superBuiltUpArea").asDouble(0.0));
				dto.setMulti_usage(property.path("usageCategory").asText("N/A"));

				// Map address
				dto.setPlot_address(locality.path("name").asText("N/A"));
				dto.setBuilding_address(locality.path("name").asText("N/A"));
				dto.setBuilding_pincode(address.path("pincode").asText("N/A"));

				// Map owner details (take first owner)
				JsonNode owners = property.path("owners");
				if (owners.isArray() && owners.size() > 0) {
					JsonNode owner = owners.get(0);
					dto.setOwner_name(owner.path("name").asText("N/A"));
					dto.setGuardian_spouse_name(owner.path("fatherOrHusbandName").asText("N/A"));
					dto.setRelationship_type(owner.path("relationship").asText("N/A"));
					dto.setGender(owner.path("gender").asText("N/A"));
				}
				 String dtoJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
	                System.out.println(dtoJson);
	                return dto;
			} else {
				System.out.println("No property found in response!");
				return dto;
			}
		}
		return dto;
		
	}

}
