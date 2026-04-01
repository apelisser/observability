package com.apelisser.observability.servicea.infrastructure.document;

import com.apelisser.observability.servicea.domain.document.Document;
import com.apelisser.observability.servicea.domain.document.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class DocumentRepositoryFake implements DocumentRepository {

    @Override
    public UUID save(Document document) {
        return UUID.randomUUID();
    }

}
