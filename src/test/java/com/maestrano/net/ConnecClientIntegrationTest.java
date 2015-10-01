package com.maestrano.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;

public class ConnecClientIntegrationTest {
    private Properties props = new Properties();
    private String groupId;

    public class CnSomeModel {}
    
    @Before
    public void beforeEach() {
        props.setProperty("app.environment", "test");
        props.setProperty("api.id", "app-1");
        props.setProperty("api.key", "gfcmbu8269wyi0hjazk4t7o1sndpvrqxl53e1");
        Maestrano.configure(props);
        
        this.groupId = "cld-3";
    }

    @Test
    public void crud_UsingMaps() throws Exception {
        // Fetch all organizations
        Map<String, Object> organizations = ConnecClient.withPreset("default").all("organizations", groupId);
//      System.out.println("Fetched organizations: " + organizations);
        // Fetched organizations: {organizations=[{name=Doe Corp Inc., id=8afd71e0-8394-0132-a4d2-2623376cdffe, group_id=cld-3, type=organizations}, ... }
        
        // Retrieve first organization
        List<Map<String, Object>> organizationsHashes = (List<Map<String, Object>>) organizations.get("organizations");
        String firstOrganizationId = (String) organizationsHashes.get(0).get("id");
        Map<String, Object> organization = (Map<String, Object>) ConnecClient.withPreset("default").retrieve("organizations", groupId, firstOrganizationId).get("organizations");
//      System.out.println("Retrieved first organization: " + organization);
        // Retrieved first organization: {name=Doe Corp Inc., id=8afd71e0-8394-0132-a4d2-2623376cdffe, group_id=cld-3, type=organizations}
        
        // Create a new organization
        Map<String, Object> newOrganization = new HashMap<String, Object>();
        newOrganization.put("name", "New Organization");
        organization = (Map<String, Object>) ConnecClient.withPreset("default").create("organizations", groupId, newOrganization).get("organizations");
//      System.out.println("Created new organization: " + organization);
        // Created new organization: {name=New Organization, id=347e0fa0-cfaf-0132-4f1a-42f46dd33bd3, group_id=cld-3, type=organizations}
        
        // Update an organization
        organization.put("industry", "Hardware");
        String organizationId = (String) organization.get("id");
        Map<String, Object> updatedOrganization = (Map<String, Object>) ConnecClient.withPreset("default").update("organizations", groupId, organizationId, organization).get("organizations");
//        System.out.println("Updated organization: " + updatedOrganization);
        // Updated organization: {name=New Organization, id=347e0fa0-cfaf-0132-4f1a-42f46dd33bd3, group_id=cld-3, industry=Hardware, type=organizations}
    }
}
