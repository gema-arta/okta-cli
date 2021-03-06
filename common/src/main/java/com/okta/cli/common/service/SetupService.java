/*
 * Copyright 2020-Present Okta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.okta.cli.common.service;

import com.okta.cli.common.config.MutablePropertySource;
import com.okta.cli.common.model.OrganizationResponse;
import com.okta.cli.common.model.RegistrationQuestions;
import com.okta.sdk.resource.application.OpenIdConnectApplicationType;

import java.io.File;
import java.io.IOException;

public interface SetupService {

    OrganizationResponse createOktaOrg(RegistrationQuestions registrationQuestions,
                                       File oktaPropsFile,
                                       boolean demo,
                                       boolean interactive) throws IOException, ClientConfigurationException;

    void verifyOktaOrg(String identifier,
                       RegistrationQuestions registrationQuestions,
                       File oktaPropsFile) throws IOException, ClientConfigurationException;

    void createOidcApplication(MutablePropertySource propertySource,
                               String oidcAppName,
                               String orgUrl,
                               String groupClaimName,
                               String issuerUri,
                               String authorizationServerId,
                               boolean interactive,
                               OpenIdConnectApplicationType appType,
                               String... redirectUris) throws IOException;
}
