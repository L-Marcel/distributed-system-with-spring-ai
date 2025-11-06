package ufrn.imd.extraction.tools;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ufrn.imd.extraction.dto.extraction.ExtractedContractDTO;
import ufrn.imd.extraction.dto.extraction.ExtractedNoticeDTO;
import ufrn.imd.extraction.dto.extraction.ExtractedNoticeTypeDTO;
import ufrn.imd.extraction.repository.VectorStoreRepository;

@Component
public class ExtractionTools {
  private static final Logger log = LoggerFactory.getLogger(
    ExtractionTools.class
  );

  private ObjectWriter writer;
  private Prompts prompts;
  private ChatClient client;
  private VectorStoreRepository vectors;

  @Autowired
  public ExtractionTools(
    ObjectMapper mapper,
    Prompts prompts,
    ChatClient client,
    VectorStoreRepository vectors
  ) {
    this.writer = mapper.writerWithDefaultPrettyPrinter();
    this.prompts = prompts;
    this.client = client;
    this.vectors = vectors;
  };

  private String documentsToString(
    List<Document> documents
  ) {
    StringBuilder builder = new StringBuilder();
    builder.append("O contexto encontrado através da busca se encontra abaixo, separado por\n");
    builder.append("---------- chunk [index]/[end_index] ----------\n");

    for(Document document : documents) {
      Integer index = (Integer) document.getMetadata().get("index");
      Integer end_index = (Integer) document.getMetadata().get("end_index");

      builder.append("\n---------- chunk ");
      builder.append(index);
      builder.append("/");
      builder.append(end_index);
      builder.append(" ----------\n");
      builder.append(document.getText());
    };

    builder.append("---------- end of result ----------");

    return builder.toString();
  };

  @Tool(
    name = "search_in_notice", 
    description = "Procura por trechos (chunks) relevantes de um edital."
  )
  public String searchInNotice(
    @ToolParam(
      required = true, 
      description = "O texto de busca usado na pesquisa por similaridade."
    ) String query,

    @ToolParam(
      required = true,
      description = "O UUID da notícia."
    ) String id,

    @ToolParam(
      required = true, 
      description = "A versão da notícia."
    ) Integer version
  ) {
    log.debug(
      """
      Tool 'search_in_notice' requested with params:

      Query: '{}'
      Id: '{}'
      Version: '{}'
      """,
      query,
      id,
      version
    );
    
    SearchRequest request = SearchRequest.from(
      this.vectors.searchByNoticeIdAndVersion(
        UUID.fromString(id),
        version
      )
    ).query(query)
    .topK(3)
    .build();

    List<Document> documents = this.vectors.getStore()
      .similaritySearch(request);

    String result = this.documentsToString(documents);

    log.debug(
      """
      Tool 'search_in_notice' returned:
      
      {}
      """,
      result
    );

    return result;
  };

  @Tool(
    name = "extract_notice_type", 
    description = """
      Recebe parte do contexto de um edital e, a partir dele, 
      tenta extrair o tipo do edital. No caso de não conseguir identificar,
      retorna UNKNOWN.
    """
  )
  public ExtractedNoticeTypeDTO extractNoticeType(
    @ToolParam(
      required = true, 
      description = "A parte do edital a ser usada como contexto."
    ) String context
  ) {
    log.debug(
      """
      Tool 'extract_notice_type' requested with params:

      Context: '{}'
      """,
      context
    );

    ExtractedNoticeTypeDTO type = this.client.prompt()
      .system(
        this.prompts.get("system_extract_type")
      ).user((prompt) -> 
        prompt.text(
          this.prompts.get("user_extract_type")
        ).param("context", context)
      ).call()
      .entity(ExtractedNoticeTypeDTO.class);

    log.debug(
      """
      Tool 'extract_notice_type' returned:
      
      {}
      """,
      type
    );

    return type;
  };

  @Tool(
    name = "extract_notice_contract", 
    description = """
      Recebe parte do contexto de um edital e, a partir dele, 
      tenta extrair dados do edital. Dados não identificados
      são retornados como null. Resposta acompanha algumas
      observações.
    """
  )
  public ExtractedContractDTO extractNoticeContract(
    @ToolParam(
      required = true, 
      description = "A parte do edital a ser usada como contexto."
    ) String context
  ) {
    log.debug(
      """
      Tool 'extract_notice_contract' requested with params:

      Context: '{}'
      """,
      context
    );
    
    ExtractedContractDTO contract = this.client.prompt()
      .system(
        this.prompts.get("system_extract_contract")
      ).user((prompt) -> 
        prompt.text(
          this.prompts.get("user_extract_contract")
        ).param("context", context)
      ).call()
      .entity(ExtractedContractDTO.class);

    try {
      log.debug(
        """
        Tool 'search_in_notice' returned:
        
        {}
        """,
        this.writer.writeValueAsString(contract)
      );
    } catch (Exception e) {
      log.debug(
        """
        Tool 'search_in_notice' returned:
        
        {}
        """,
        contract
      );
    };

    return contract;
  };

  @Tool(
    name = "save_extracted_notice", 
    description = """
      Atualiza um edital com base nos dados extraídos de seu 
      arquivo.
    """
  ) public void saveExtractedNotice(
    ExtractedNoticeDTO notice
  ) {
    try {
      log.debug(
        """
        Tool 'save_extracted_notice' returned:
        
        {}
        """,
        this.writer.writeValueAsString(notice)
      );
    } catch (Exception e) {
      log.debug(
        """
        Tool 'save_extracted_notice' returned:
        
        {}
        """,
        notice
      );
    };
  };
};
