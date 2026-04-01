package com.apelisser.observability.servicea.domain.document;

import java.io.InputStream;

public interface DocumentStorage {

    void save(String filename, InputStream content);

}
