package com.seneau.security;

import com.google.common.collect.ObjectArrays;
import org.apache.commons.lang.ArrayUtils;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.ws.rs.HttpMethod;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {


    @Autowired
    public  KeycloakClientRequestFactory keycloakClientRequestFactory;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        String[] roles1= {"DRHT","DIRECTEUR","MANAGER_PAIE"};
        String[] roles2 = {"MANAGER","DRHT","DIRECTEUR","MANAGER_PAIE","COLLABORATEUR","AGENT_PAIE"};
        String[] roles3 = {"DRHT","DIRECTEUR","MANAGER_PAIE","AGENT_PAIE"};
        String[] roles4= {"MANAGER","DRHT","DIRECTEUR","MANAGER_PAIE","AGENT_PAIE" }; // manager plus spécifiquement
        String[] roles5= {"DRHT","DIRECTEUR","MANAGER_PAIE","AGENT_PAIE" }; // directeur plus spécifiquement
        http.authorizeRequests().anyRequest().permitAll();
/*        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/conges").hasAnyRole(roles3)
                .antMatchers(HttpMethod.POST,HttpMethod.PUT, "/api/conges").hasAnyRole(roles1)
                .antMatchers(HttpMethod.GET, "/api/conges/{id}").hasAnyRole(roles2)
                .antMatchers(HttpMethod.DELETE, "/api/conges/{id}").hasAnyRole(roles1)
                .antMatchers(HttpMethod.GET, "/api/conges/update/an", "/api/conges/update/an/{years}", "/api/conges/count/mois").hasAnyRole(roles3)
                .antMatchers(HttpMethod.GET, "/api/conges/an", "/api/conges/an/{years}", "/api/conges/count/mois").hasAnyRole(roles3)
                .antMatchers(HttpMethod.GET, "/api/conges/direction/{matricule}").hasAnyRole("DIRECTEUR")
                .antMatchers(HttpMethod.GET, "/api/conges/direction/count").hasAnyRole(roles1)
                .antMatchers(HttpMethod.GET, "/api/conges/direction/count/mois/{matricule}").hasAnyRole("DIRECTEUR","DRHT")
                .antMatchers(HttpMethod.GET, "/api/conges/direction/{matricule}").hasAnyRole("DIRECTEUR","DRHT")
                .antMatchers(HttpMethod.GET, "/api/conges/matricule/{matricule}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/conges/mois", "/api/conges/mois/{month_year}").hasAnyRole(roles1)
                .antMatchers(HttpMethod.GET, "/api/conges/mois/responsable/{matricule}","/api/conges/mois/responsable/{matricule}", "/api/conges/mois/{month_year}").hasAnyRole("DIRECTEUR","MANAGER","MANAGER_PAIE")
                .antMatchers(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, "/api/conge_data", "/api/conge_data/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/conge-data/{id}", "/api/conge-data", HttpMethod.POST, "/api/conge-data", HttpMethod.PUT, "/api/conge-data").authenticated()
                .antMatchers(HttpMethod.GET, "/api/conge-data/matricule/{matricule}").hasAnyRole(roles3)

                // PATHS RAPPELS
                .antMatchers(HttpMethod.GET,"/api/rappels").hasAnyRole(roles3)
                .antMatchers(HttpMethod.POST,HttpMethod.PUT,"/api/rappels").hasAnyRole(roles2) // avec resctriction
                .antMatchers(HttpMethod.GET,HttpMethod.DELETE,"/api/rappels/{id}").hasAnyRole(roles2) // avec resctriction
                .antMatchers("/api/rappels/an","/api/rappels/an/{an}").hasAnyRole(roles3)
                .antMatchers("/api/rappels/directeur/count/mois/{matricule}").hasAnyRole(roles3)
                .antMatchers("/api/rappels/direction/year/{matricule}").hasAnyRole(roles3)
                .antMatchers("/api/rappels/mois","/api/rappels/mois/{year}").hasAnyRole(roles3)
                .antMatchers("/api/rappels/directeur/count/mois/{matricule}").hasAnyRole(roles3)
                .antMatchers("/api/rappels/direction/year/{matricule}").hasAnyRole(roles5)
                .antMatchers("/api/rappels/manager/count/mois/{matricule}","/api/rappels/manager/year/{matricule}").hasAnyRole(roles4)

                // PATH Récupérations
                .antMatchers(HttpMethod.GET,"/api/recuperations").hasAnyRole(roles3)
                .antMatchers(HttpMethod.POST,HttpMethod.PUT,"/api/recuperations").authenticated() // avec resctriction
                .antMatchers(HttpMethod.GET,HttpMethod.DELETE,"/api/recuperations/{id}").authenticated() // avec resctriction
                .antMatchers(HttpMethod.GET, "/api/recuperations/{id}").hasAnyRole(roles5)
                .antMatchers("/api/recuperations/matricule/{matricule}").hasAnyRole(roles4)
                .antMatchers("/api/recuperations/manager/{matricule}").hasAnyRole(roles4)
                .antMatchers("/api/recuperations/matricule/{matricule}").hasAnyRole(roles4);*/
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate getKeycloakrestTemplate(){
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }
/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/test/anonymous").permitAll()
                .antMatchers("/test/user").hasAnyRole("user")
                .antMatchers("/test/admin").hasAnyRole("admin")
                .antMatchers("/test/all-user").hasAnyRole("user","admin")
                .anyRequest()
                .permitAll();
        http.csrf().disable();
    }*/
}
