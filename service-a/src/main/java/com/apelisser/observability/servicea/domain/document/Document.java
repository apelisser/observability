package com.apelisser.observability.servicea.domain.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Document {

    private UUID id;
    private String filename;
    private String contentType;
    private Long size;
    private InputStream content;

}
