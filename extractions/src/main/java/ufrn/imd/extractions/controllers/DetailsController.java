package ufrn.imd.extractions.controllers;

import java.util.List;

import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/details")
public class DetailsController {
  private SyncMcpToolCallbackProvider tools;

  @Autowired
  public DetailsController(
    SyncMcpToolCallbackProvider tools
  ) {
    this.tools = tools;
  };

  @GetMapping("/tools")
  public ResponseEntity<List<String>> getTools() {
    return ResponseEntity.ok(
      List.of(
        tools.getToolCallbacks()
      ).stream()
        .map((tool) -> tool.getToolDefinition().name())
        .toList()
    );
  };
};
