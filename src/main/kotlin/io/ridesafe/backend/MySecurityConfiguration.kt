package io.ridesafe.backend

import io.ridesafe.backend.filters.DeviceIdHeaderFilter
import io.ridesafe.backend.security.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.savedrequest.NullRequestCache

/**
 * Created by evoxmusic on 12/04/16.
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class MySecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private var environment: Environment? = null
    @Autowired
    private var authenticationFailureHandler: AuthenticationFailureHandler? = null
    @Autowired
    private var authenticationProvider: AuthenticationProvider? = null
    @Autowired
    private var userDetailsService: UserDetailsService? = null

    fun myCORSFilter() = MyCORSFilter(environment?.getProperty("ridesafe.web.host"))

    fun deviceIdHeaderFilter() = DeviceIdHeaderFilter()

    fun tokenValidationFilter() = MyTokenValidationFilter(authenticationFailureHandler, authenticationProvider, userDetailsService)

    @Bean
    open fun authenticationFailureHandler(): AuthenticationFailureHandler = MyAuthenticationFailureHandler()

    @Bean
    open fun authenticationSuccessHandler(): AuthenticationSuccessHandler = MyAuthenticationSuccessHandler()

    @Bean
    override fun userDetailsService(): UserDetailsService = MyUserDetailsService()

    @Bean
    open fun authenticationProvider(): AuthenticationProvider = MyAuthenticationProvider()

    public override fun configure(auth: AuthenticationManagerBuilder?) {

    }

    public override fun configure(http: HttpSecurity) {

        http.addFilterAfter(tokenValidationFilter(), BasicAuthenticationFilter::class.java)
        http.addFilterBefore(myCORSFilter(), MyTokenValidationFilter::class.java)
        http.addFilterAfter(deviceIdHeaderFilter(), MyCORSFilter::class.java)

        http.authorizeRequests().antMatchers("/api/v1/**").permitAll()
                .antMatchers("/api/v1/login", "/api/v1/logout").hasAnyRole(Security.USER, Security.ADMIN)
                .antMatchers("/api/v1/admin/**").hasRole(Security.ADMIN)
                .and().requestCache().requestCache(NullRequestCache())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic()
                .and().csrf().disable()

        http.exceptionHandling().authenticationEntryPoint(MyAuthenticationEntryPoint())
    }


}
