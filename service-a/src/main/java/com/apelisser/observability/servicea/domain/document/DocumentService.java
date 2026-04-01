package com.apelisser.observability.servicea.domain.document;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class DocumentService {

    private final Counter successCounter;
    private final Counter errorCounter;
    private final DistributionSummary successFileSizeSummary;
    private final DistributionSummary errorFileSizeSummary;

    private final DocumentStorage storage;
    private final DocumentRepository documentRepository;

    public DocumentService(MeterRegistry meterRegistry, DocumentStorage storage, DocumentRepository documentRepository) {
        this.storage = storage;
        this.documentRepository = documentRepository;

        this.successCounter = Counter.builder("upload.file")
            .description("Upload success file counter")
            .tag("result", "success")
            .register(meterRegistry);

        this.errorCounter = Counter.builder("upload.file")
            .description("Upload error file counter")
            .tag("result", "error")
            .register(meterRegistry);

        this.successFileSizeSummary = DistributionSummary.builder("upload.file.size")
            .description("Upload file size in bytes")
            .baseUnit("bytes")
            .tag("result", "success")
            .publishPercentileHistogram()
            .register(meterRegistry);

        this.errorFileSizeSummary = DistributionSummary.builder("upload.file.size")
            .description("Upload file size in bytes")
            .baseUnit("bytes")
            .tag("result", "error")
            .publishPercentileHistogram()
            .register(meterRegistry);
    }

    @Timed(value = "upload.file.duration", histogram = true)
    public UUID upload(Document document, String owner) {
        log.info("Uploading document {} for owner {}", document.getFilename(), owner);

        try {
            storage.save(document.getFilename(), document.getContent());

            UUID documentId = documentRepository.save(document);

            successCounter.increment();
            successFileSizeSummary.record(document.getSize());

            log.info("Document created with id={}", documentId);
            return documentId;
        } catch (Exception e) {
            errorCounter.increment();
            errorFileSizeSummary.record(document.getSize());
            log.error("Failed to upload document");
            throw e;
        }
    }

}
