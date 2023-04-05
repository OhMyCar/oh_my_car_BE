package com.hotsix.omc.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final static String STATUS = "status";
    private final static String ERROR = "error";
    private final static String MESSAGE = "message";
    private final static String PATH = "path";
    private final static String UNAUTHORIZED_ERROR = "Unauthorized";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e){
            setErrorResponse(request, response, e);
        }
    }

    private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, JwtException e) throws IOException{
        final Map<String, Object> body = new HashMap<>();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        body.put(STATUS, HttpServletResponse.SC_UNAUTHORIZED);
        body.put(ERROR, UNAUTHORIZED_ERROR);
        body.put(MESSAGE, e.getMessage());
        body.put(PATH, request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(response.getOutputStream(), body);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
