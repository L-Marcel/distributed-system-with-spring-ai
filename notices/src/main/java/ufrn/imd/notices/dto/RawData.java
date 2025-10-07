package ufrn.imd.notices.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.document.Document;

public record RawData(
  UUID uuid,
  List<Document> vectors
) {};
