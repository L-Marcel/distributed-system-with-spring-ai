package ufrn.imd.extraction;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import jakarta.transaction.Transactional;
import ufrn.imd.extraction.dto.extraction.ExtractedContractDTO;
import ufrn.imd.extraction.dto.extraction.ExtractedNoticeDTO;
import ufrn.imd.extraction.errors.NoticeNotFound;
import ufrn.imd.extraction.models.Contract;
import ufrn.imd.extraction.models.Note;
import ufrn.imd.extraction.models.Notice;
import ufrn.imd.extraction.repository.VectorStoreRepository;
import ufrn.imd.extraction.repository.NoticesRepository;

@Component
public class Tools {
  private static final Logger log = LoggerFactory.getLogger(
    Tools.class
  );

  private ObjectWriter writer;
  private VectorStoreRepository vectors;
  private NoticesRepository notices;

  
  @Autowired
  public Tools(
    ObjectMapper mapper,
    VectorStoreRepository vectors,
    NoticesRepository notices
  ) {
    this.writer = mapper.writerWithDefaultPrettyPrinter();
    this.vectors = vectors;
    this.notices = notices;
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
    name = "save_notice", 
    description = """
      Atualiza um edital com base nos dados extraídos.
      Retorna uma mensagem de erro ou os dados do edital atualizado.
    """
  ) @Transactional 
  public String saveNotice(
    @ToolParam(
      required = true,
      description = "Dados extraídos do edital"
    ) ExtractedNoticeDTO notice
  ) {
    try {
      log.debug(
        """
        Tool 'save_notice' received:
        
        {}
        """,
        this.writer.writeValueAsString(notice)
      );

      Notice _notice = this.notices.findById(
        notice.id()
      ).orElseThrow(NoticeNotFound::new);

      _notice.setStatus(notice.status());
      _notice.setType(notice.type());

      if(notice.notes() != null) _notice.getNotes().addAll(notice.notes().stream().map((note) -> {
        Note _note = new Note();
        _note.setContent(note.content());
        return _note;
      }).toList());

      this.notices.save(_notice);

      return this.writer.writeValueAsString(notice);
    } catch (Exception e) {
      return "Falha ao atualizar o edital: " + e.getMessage();
    }
  };

  // TODO - Salvar as empresas
  // TODO - Voltar para o transacional
  @Tool(
    name = "save_contract", 
    description = """
      Atualiza o contrato de um edital com base nos dados extraídos.
      Retorna uma mensagem de erro ou os dados do contrato e edital atualizados.
    """
  ) //@Transactional 
  public String saveContract(
    @ToolParam(
      required = true,
      description = "Dados extraídos do contrato de um edital"
    ) ExtractedContractDTO contract
  ) {
    try {
      log.debug(
        """
        Tool 'save_contract' received:
        
        {}
        """,
        this.writer.writeValueAsString(contract)
      );

      Notice notice = this.notices.findById(
        contract.notice()
      ).orElseThrow(NoticeNotFound::new);

      Contract _contract = notice.getContract();
      if(_contract == null) _contract = new Contract();
      if(contract.value() != null) _contract.setValue(contract.value());
      if(contract.currency() != null) _contract.setCurrency(contract.currency());
      if(contract.location() != null) _contract.setLocation(contract.location());

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      if(contract.startDate() != null) _contract.setStartDate(Date.valueOf(
        LocalDate.parse(contract.startDate(), formatter)
      ));

      if(contract.endDate() != null) _contract.setEndDate(Date.valueOf(
        LocalDate.parse(contract.endDate(), formatter)
      ));

      notice.setContract(_contract);
      this.notices.save(notice);
      
      return this.writer.writeValueAsString(contract);
    } catch (Exception e) {
      return "Falha ao atualizar o contrato: " + e.getMessage();
    }
  };
};
