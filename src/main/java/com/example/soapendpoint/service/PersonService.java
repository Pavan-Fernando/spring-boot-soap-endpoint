package com.example.soapendpoint.service;

import com.example.soapendpoint.model.entity.PersonEntity;
import com.example.soapendpoint.model.repository.PersonRepository;
import com.example.soapendpoint.wsdl.Person;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
public class PersonService {

    private final PersonRepository repo;
    private final Validator validator;  // Spring injects this

    public PersonEntity save(Person personDto) {
        // Convert DTO → Entity
        PersonEntity entity = PersonEntity.builder()
                .name(personDto.getName())
                .email(personDto.getEmail())
                .build();

        // Validate using Bean Validation
        Set<ConstraintViolation<PersonEntity>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return repo.save(entity);
    }

    public PersonEntity findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found: " + id));
    }

    public Person toDto(PersonEntity entity) {
        Person dto = new Person();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        return dto;
    }
}
