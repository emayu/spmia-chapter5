package com.thoughtmechanix.licenses.clients;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.services.LicenseService;
import java.util.Random;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LicenseService.class);
    
    

    @HystrixCommand(
            fallbackMethod = "buildFallbackOrganization"
//            commandProperties = {
//                     @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000")}
    )
    public Organization getOrganization(String organizationId){
        randomlyRunLong();
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://organizationservice/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);
        
        return restExchange.getBody();
    }
    
    private Organization buildFallbackOrganization(String organizationId){
        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Sorry no organizacion information currently available");
        organization.setContactEmail("");
        organization.setContactName("");
        logger.debug("returning info " + organization.toString());
        return organization;
    }
    
    private void randomlyRunLong(){
      Random rand = new Random();

      int randomNum = rand.nextInt((3 - 1) + 1) + 1;

      if (randomNum==3){ 
        logger.debug("Randomly sleeping");
        sleep();
      }
    }

    private void sleep(){
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
