package com.udd.PropertyTax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController

@RequestMapping("/naksha")
public class NakshaProject {

	@Autowired
	private AuthRequestService authRequestService;

	@GetMapping("/getProperty")
	public String specialCategory(@RequestParam("ulb") String ulbName, @RequestParam("propertyid") String propertyid) {
		String a = null;
		return a;
	}

	public NakshaProject(AuthRequestService authRequestService) {
		this.authRequestService = authRequestService;
	}

	@GetMapping("/get-token")
	public String getToken() throws Exception {
		return authRequestService.getAuthToken();
	}

	@GetMapping("/searchProperty")
	public PropertyResponse getProperty(@RequestParam String tenantId, @RequestParam String propertyId) throws Exception {
		String token = authRequestService.getAuthToken();
		System.out.println(token);
		
		return authRequestService.fetchProperty(tenantId, propertyId, token);
	}

}
