package com.example.soapendpoint.config;

import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapFaultConfig {

    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
        SoapFaultMappingExceptionResolver resolver = new SoapFaultMappingExceptionResolver();
        SoapFaultDefinition fault = new SoapFaultDefinition();
        fault.setFaultCode(SoapFaultDefinition.CLIENT);
        resolver.setDefaultFault(fault);
        return resolver;
    }
}
