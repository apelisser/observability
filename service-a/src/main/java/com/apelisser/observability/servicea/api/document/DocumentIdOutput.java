package com.apelisser.observability.servicea.api.document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DocumentIdOutput {

    private UUID id;

    public DocumentIdOutput(UUID id) {
        this.id = id;
    }

}
