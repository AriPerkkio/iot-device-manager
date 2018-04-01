package web.security;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeviceAuthenticationKeyFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authenticationKey = request.getHeader("X-Authentication-Key");

        System.out.println("Auth");
        System.out.println(authenticationKey);

        if(authenticationKey == null) {
            throw new SecurityException();
        }


        filterChain.doFilter(request, response);
    }
}
