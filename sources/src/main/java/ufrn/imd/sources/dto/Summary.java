package ufrn.imd.sources.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.document.Document;

public record Summary(
  UUID uuid,
  List<Document> documents
) {};
