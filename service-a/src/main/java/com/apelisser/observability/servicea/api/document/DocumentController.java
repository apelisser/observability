package com.apelisser.observability.servicea.api.document;

import com.apelisser.observability.servicea.domain.document.Document;
import com.apelisser.observability.servicea.domain.document.DocumentService;
import com.apelisser.observability.servicea.infrastructure.document.DocumentStorageFake;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentIdOutput uploadDocument(DocumentInput input) throws IOException {
        Document document = Document.builder()
            .filename(input.getFilename())
            .contentType(input.getContentType())
            .content(input.getFile().getInputStream())
            .size(input.getFile().getSize())
            .build();

        UUID documentId;
        documentId = documentService.upload(document, input.getOwner());
        return new DocumentIdOutput(documentId);
    }

    @PatchMapping(value = "/error-percentage", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUploadErrorPercentage(@RequestBody DocumentErrorPercentageInput input) {
        DocumentStorageFake.updateUploadErrorPercentage(input.getPercentage());
    }

}
