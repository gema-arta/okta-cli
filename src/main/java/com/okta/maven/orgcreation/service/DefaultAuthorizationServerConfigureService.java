package com.okta.maven.orgcreation.service;

import com.okta.sdk.client.Client;
import com.okta.sdk.resource.ExtensibleResource;
import com.okta.sdk.resource.ResourceException;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.StringUtils;

import java.util.Collections;

@Component(role = AuthorizationServerConfigureService.class)
public class DefaultAuthorizationServerConfigureService implements AuthorizationServerConfigureService {

    @Override
    public boolean createGroupClaim(Client client, String groupClaimName, String authorizationServerId) {

        if (StringUtils.isEmpty(authorizationServerId)) {
            authorizationServerId = "default";
        }

        // attempt to create the group claim
        ExtensibleResource claimResource = client.instantiate(ExtensibleResource.class);
        claimResource.put("name", groupClaimName);
        claimResource.put("status", "ACTIVE");
        claimResource.put("claimType", "RESOURCE");
        claimResource.put("valueType", "GROUPS");
        claimResource.put("value", ".*");
        claimResource.put("alwaysIncludeInToken", true);
        claimResource.put("group_filter_type", "REGEX");
        ExtensibleResource conditions = client.instantiate(ExtensibleResource.class);
        conditions.put("scopes", Collections.emptyList());
        claimResource.put("conditions", conditions);

        // The Okta SDK does NOT support the AuthorizationServers endpoints yet
        // and it cannot parse a generic response when List<ExtensibleResource> is the type
        // TODO: update this when the SDK supports the authorization servers api
        try {
            client.http()
                    .setBody(claimResource)
                    .post("/api/v1/authorizationServers/" + authorizationServerId + "/claims", ExtensibleResource.class);
            return true;
        } catch (ResourceException e) {
            // See above TODO, make a best effort attempt to NOT fail
            // since we are blindly creating a new claim, check to see if the error message
            // indicates the 'name' field
            // NOTE: it is possible to hit false positives for this, though unlikely for this use case
            // we will return false here and the caller can print a WARNING message
            if (e.getStatus() == 400
                && e.getError().getMessage().equals("Api validation failed: name")) {
                return false;
            }
            // rethrow
            throw e;
        }
    }
}
