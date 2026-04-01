package com.apelisser.observability.servicea.domain.document;

import java.util.UUID;

public interface DocumentRepository {

    UUID save(Document document);

}
