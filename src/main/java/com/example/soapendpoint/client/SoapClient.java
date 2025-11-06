package com.example.soapendpoint.client;

import com.example.soapendpoint.wsdl.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class SoapClient extends WebServiceGatewaySupport {

    public GetPersonResponse getPerson(long id) {
        GetPersonRequest request = new GetPersonRequest();
        request.setId(id);
        return (GetPersonResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:8080/ws", request);
    }

    public CreatePersonResponse createPerson(String name, String email) {
        CreatePersonRequest request = new CreatePersonRequest();
        request.setName(name);
        request.setEmail(email);
        return (CreatePersonResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:8080/ws", request);
    }
}
