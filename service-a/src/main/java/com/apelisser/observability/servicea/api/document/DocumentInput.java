package com.apelisser.observability.servicea.api.document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DocumentInput {

    private String filename;
    private String contentType;
    private String owner;
    private MultipartFile file;

}
