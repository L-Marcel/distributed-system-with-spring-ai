package ufrn.imd.sources.workflows.chain;

public final class ChainWorkflowBuilder {
  private ChainHandler first;
  private ChainHandler last;

  public ChainWorkflowBuilder chain(
    ChainHandler handler
  ) {
    if(this.first == null) this.first = handler;
    if(this.last != null) this.last.setNext(handler);
    this.last = handler;
    return this;
  };

  public ChainWorkflow build() {
    return new ChainWorkflow(this.first);
  };
};
