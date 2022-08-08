/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orangetv.cloud.videostore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ResourceServerConfig {

    // @formatter:off
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/actuator/health")
                    .permitAll()
                .mvcMatchers(HttpMethod.GET,"/**")
                    .access("hasAuthority('SCOPE_video.metadata.read')")
                .mvcMatchers(HttpMethod.POST,"/**")
                    .access("hasAuthority('SCOPE_video.metadata.write')")
                .mvcMatchers(HttpMethod.DELETE,"/**")
                    .access("hasAuthority('SCOPE_video.metadata.write')")
                .mvcMatchers(HttpMethod.PUT,"/**")
                    .access("hasAuthority('SCOPE_video.metadata.write')")
                .and()
            .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
    // @formatter:on
}
