package ufrn.imd.notices.controllers;

import java.util.List;

import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.mcp.client.autoconfigure.properties.McpClientCommonProperties.Toolcallback;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufrn.imd.notices.configurations.AppConfiguration;

@RestController
@RequestMapping("/configuration")
public class ConfigurationsController {
  private AppConfiguration configuration;
  private SyncMcpToolCallbackProvider tools;

  @Autowired
  public ConfigurationsController(
    AppConfiguration configuration,
    SyncMcpToolCallbackProvider tools
  ) {
    this.configuration = configuration;
    this.tools = tools;
  };

  @GetMapping("/version")
  public ResponseEntity<String> getVersion() {
    return ResponseEntity.ok(this.configuration.getVersion());
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
