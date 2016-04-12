package io.ridesafe.backend

import io.ridesafe.backend.security.MyAuthenticationFailureHandler
import io.ridesafe.backend.security.MyAuthenticationSuccessHandler
import io.ridesafe.backend.security.Security
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.NullRequestCache

/**
 * Created by evoxmusic on 12/04/16.
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class MySecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Bean
    open fun authenticationFailureHandler(): AuthenticationFailureHandler = MyAuthenticationFailureHandler()

    @Bean
    open fun authenticationSuccessHandler(): AuthenticationSuccessHandler = MyAuthenticationSuccessHandler()

    public override fun configure(auth: AuthenticationManagerBuilder?) {

    }

    public override fun configure(http: HttpSecurity) {

        http.authorizeRequests().antMatchers("/api/v1/**").permitAll()
                .antMatchers("/api/v1/admin/**").hasRole(Security.ADMIN)
                .and().requestCache().requestCache(NullRequestCache())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic()
                .and().csrf().disable()
    }


}
