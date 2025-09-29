package ufrn.imd.sources.workflows.chain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class ChainWorkflow {
  protected ChainHandler first;

  public String execute(String prompt) {
    if(this.first != null) return this.first.handle(prompt);
    else return prompt;
  };

  public static ChainWorkflowBuilder builder() {
    return new ChainWorkflowBuilder();
  };
};
