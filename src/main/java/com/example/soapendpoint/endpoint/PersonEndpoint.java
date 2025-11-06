package com.example.soapendpoint.endpoint;

import com.example.soapendpoint.model.repository.PersonRepository;
import com.example.soapendpoint.service.PersonService;
import com.example.soapendpoint.wsdl.*;
import jakarta.validation.ConstraintViolationException;
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
    private final PersonService personService;

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetPersonRequest")
    @ResponsePayload
    public GetPersonResponse getPerson(@RequestPayload GetPersonRequest request) {
        GetPersonResponse response = new GetPersonResponse();

        repository.findById(request.getId()).ifPresentOrElse(
                dbPersonEntity -> {
                    com.example.soapendpoint.wsdl.Person  wsdlPerson = new com.example.soapendpoint.wsdl.Person();
                    wsdlPerson.setId(dbPersonEntity.getId());
                    wsdlPerson.setName(dbPersonEntity.getName());
                    wsdlPerson.setEmail(dbPersonEntity.getEmail());
                    response.setPerson(wsdlPerson);
                },
                () -> { /* Not found - empty response */ }
        );

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "CreatePersonRequest")
    @ResponsePayload
    public CreatePersonResponse createPerson(@RequestPayload CreatePersonRequest request) {
        try {
            Person personDto = new Person();
            personDto.setName(request.getName());
            personDto.setEmail(request.getEmail());

            var savedEntity = personService.save(personDto);
            Person responseDto = personService.toDto(savedEntity);

            CreatePersonResponse response = new CreatePersonResponse();
            response.setPerson(responseDto);
            return response;

        } catch (ConstraintViolationException ex) {
            throw new IllegalArgumentException(
                    ex.getConstraintViolations().iterator().next().getMessage()
            );
        }
    }
}
