package ufrn.imd.extraction;

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

import ufrn.imd.commons.dto.extraction.ExtractedAddressDTO;
import ufrn.imd.commons.dto.extraction.ExtractedContractDTO;
import ufrn.imd.commons.dto.extraction.ExtractedNoticeDTO;
import ufrn.imd.commons.dto.extraction.ExtractedRepresentativeDTO;
import ufrn.imd.commons.dto.extraction.ExtractedVacanciesDTO;
import ufrn.imd.commons.models.Company;
import ufrn.imd.commons.models.Notice;
import ufrn.imd.commons.repository.VectorStoreRepository;
import ufrn.imd.extraction.services.ExtractionsService;

@Component
public class Tools {
  private static final Logger log = LoggerFactory.getLogger(
    Tools.class
  );

  private ObjectWriter writer;
  private VectorStoreRepository vectors;
  private ExtractionsService extractions;
  
  @Autowired
  public Tools(
    ObjectMapper mapper,
    VectorStoreRepository vectors,
    ExtractionsService extractions
  ) {
    this.writer = mapper.writerWithDefaultPrettyPrinter();
    this.vectors = vectors;
    this.extractions = extractions;
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
  ) public String searchInNotice(
    @ToolParam(
      required = true, 
      description = "O texto de busca usado na pesquisa por similaridade."
    ) String query,

    @ToolParam(
      required = true,
      description = "O UUID da notícia."
    ) String id
  ) {
    log.debug(
      """
      Tool 'search_in_notice' requested with params:

      Query: '{}'
      Id: '{}'
      """,
      query,
      id
    );
    
    SearchRequest request = SearchRequest.from(
      this.vectors.searchByNoticeId(
        UUID.fromString(id)
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
  ) public String saveNotice(
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

      Notice _notice = this.extractions.updateFrom(notice);
      return this.writer.writeValueAsString(_notice);
    } catch (Exception e) {
      return "Falha ao atualizar o edital: " + e.getMessage();
    }
  };

  @Tool(
    name = "save_contract", 
    description = """
      Atualiza ou cria o contrato de um edital com base nos dados extraídos.
      Retorna uma mensagem de erro ou os dados do contrato e edital atualizados.
    """
  ) public String saveContract(
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

      Notice notice = this.extractions.updateFrom(contract);
      return this.writer.writeValueAsString(notice);
    } catch (Exception e) {
      return "Falha ao atualizar o contrato: " + e.getMessage();
    }
  };

  @Tool(
    name = "save_representative", 
    description = """
      Atualiza ou cria o representante de uma empresa com base nos dados extraídos.
      Retorna uma mensagem de erro ou os dados da empresa e representante atualizados.
    """
  ) public String saveRepresentative(
    @ToolParam(
      required = true,
      description = "Dados extraídos do representante de uma empresa"
    ) ExtractedRepresentativeDTO representative
  ) {
    try {
      log.debug(
        """
        Tool 'save_representative' received:
        
        {}
        """,
        this.writer.writeValueAsString(representative)
      );

      Company company = this.extractions.updateFrom(representative);
      return this.writer.writeValueAsString(company);
    } catch (Exception e) {
      return "Falha ao atualizar o contrato: " + e.getMessage();
    }
  };

  @Tool(
    name = "save_address", 
    description = """
      Atualiza ou cria endereço de uma empresa com base nos dados extraídos.
      Retorna uma mensagem de erro ou os dados da empresa e endereço atualizados.
    """
  ) public String saveAddress(
    @ToolParam(
      required = true,
      description = "Dados extraídos do endereço de uma empresa"
    ) ExtractedAddressDTO address
  ) {
    try {
      log.debug(
        """
        Tool 'save_address' received:
        
        {}
        """,
        this.writer.writeValueAsString(address)
      );

      Company company = this.extractions.updateFrom(address);
      return this.writer.writeValueAsString(company);
    } catch (Exception e) {
      return "Falha ao atualizar o contrato: " + e.getMessage();
    }
  };

  @Tool(
    name = "save_vacancies", 
    description = """
      Atualiza ou cria vagas de um edital com base nos dados extraídos.
      Retorna uma mensagem de erro ou os dados das vagas e edital atualizados.
    """
  ) public String saveVacancies(
    @ToolParam(
      required = true,
      description = "Dados extraídos das vagas de um edital"
    ) ExtractedVacanciesDTO vacancies
  ) {
    try {
      log.debug(
        """
        Tool 'save_vacancies' received:
        
        {}
        """,
        this.writer.writeValueAsString(vacancies)
      );

      Notice notice = this.extractions.updateFrom(vacancies);
      return this.writer.writeValueAsString(notice);
    } catch (Exception e) {
      return "Falha ao atualizar o contrato: " + e.getMessage();
    }
  };
};
