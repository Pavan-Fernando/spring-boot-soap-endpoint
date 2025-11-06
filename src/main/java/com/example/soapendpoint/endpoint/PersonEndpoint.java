package com.example.soapendpoint.endpoint;

import com.example.soapendpoint.model.repository.PersonRepository;
import com.example.soapendpoint.model.entity.Person;
import com.example.soapendpoint.wsdl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class PersonEndpoint {

    private static final String NAMESPACE = "http://example.com/soapdemo/wsdl";
    private final PersonRepository repository;

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetPersonRequest")
    @ResponsePayload
    public GetPersonResponse getPerson(@RequestPayload GetPersonRequest request) {
        GetPersonResponse response = new GetPersonResponse();

        repository.findById(request.getId()).ifPresentOrElse(
                dbPerson -> {
                    com.example.soapendpoint.wsdl.Person  wsdlPerson = new com.example.soapendpoint.wsdl.Person();
                    wsdlPerson.setId(dbPerson.getId());
                    wsdlPerson.setName(dbPerson.getName());
                    wsdlPerson.setEmail(dbPerson.getEmail());
                    response.setPerson(wsdlPerson);
                },
                () -> { /* Not found - empty response */ }
        );

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "CreatePersonRequest")
    @ResponsePayload
    public CreatePersonResponse createPerson(@RequestPayload CreatePersonRequest request) {
        CreatePersonResponse response = new CreatePersonResponse();

        // Check if email exists
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Save to DB
        Person dbPerson = new Person();
        dbPerson.setName(request.getName());
        dbPerson.setEmail(request.getEmail());

        Person saved = repository.save(dbPerson);

        // Convert to WSDL response
        com.example.soapendpoint.wsdl.Person wsdlPerson = new com.example.soapendpoint.wsdl.Person();
        wsdlPerson.setId(saved.getId());
        wsdlPerson.setName(saved.getName());
        wsdlPerson.setEmail(saved.getEmail());

        response.setPerson(wsdlPerson);
        return response;
    }
}
