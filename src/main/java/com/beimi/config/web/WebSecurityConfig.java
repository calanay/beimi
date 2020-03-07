package com.beimi.config.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(tokenInfoTokenFilterSecurityInterceptor() , BasicAuthenticationFilter.class)
	        .antMatcher("*/*").authorizeRequests()
	        .anyRequest().permitAll()
	        .and().addFilterAfter(csrfHeaderFilter(), BasicAuthenticationFilter.class)
	        .addFilterAfter(apiTokenFilterSecurityInterceptor(), BasicAuthenticationFilter.class);
    }
    @Bean
    public Filter tokenInfoTokenFilterSecurityInterceptor() throws Exception
    {
        RequestMatcher autconfig = new AntPathRequestMatcher("/autoconfig/**");
        RequestMatcher configprops = new AntPathRequestMatcher("/configprops/**");
        RequestMatcher beans = new AntPathRequestMatcher("/beans/**");
        RequestMatcher dump = new AntPathRequestMatcher("/dump/**");
        RequestMatcher env = new AntPathRequestMatcher("/env/**");
        RequestMatcher health = new AntPathRequestMatcher("/health/**");
        RequestMatcher info = new AntPathRequestMatcher("/info/**");
        RequestMatcher mappings = new AntPathRequestMatcher("/mappings/**");
        RequestMatcher metrics = new AntPathRequestMatcher("/metrics/**");
        RequestMatcher trace = new AntPathRequestMatcher("/trace/**");
        RequestMatcher druid = new AntPathRequestMatcher("/druid/**");
        
        return new DelegateRequestMatchingFilter(autconfig , configprops , beans , dump , env , health , info , mappings , metrics , trace, druid);
    }
    /**
     * 手机注册 和 游客注册 保留两个 免验证 访问
     * @return
     * @throws Exception
     */
    @Bean
    public Filter apiTokenFilterSecurityInterceptor() throws Exception
    {
        return new ApiRequestMatchingFilter(new AntPathRequestMatcher[]{new AntPathRequestMatcher("/api/register"),new AntPathRequestMatcher("/api/guest")} , new AntPathRequestMatcher("/api/**"));
    }
    
    private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {

                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null
                            && !token.equals(cookie.getValue())) {

                        // Token is being added to the XSRF-TOKEN cookie.
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }
}
