#!/bin/bash
#
# Copyright 2017-Present Okta, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#Using xmllint is faster than invoking maven
ARTIFACT_VERSION="$(xmllint --xpath "//*[local-name()='project']/*[local-name()='version']/text()" pom.xml)"
IS_RELEASE=$([ "${ARTIFACT_VERSION/SNAPSHOT}" == "${ARTIFACT_VERSION}" ] && [ "${BRANCH}" == 'master' ] && echo 'true')
export ARTIFACT_VERSION
export IS_RELEASE

echo "Build configuration:"
echo "Version:             ${ARTIFACT_VERSION}"
echo "Is release:          ${IS_RELEASE:-false}"
echo
echo "Java Version:"
java -version
