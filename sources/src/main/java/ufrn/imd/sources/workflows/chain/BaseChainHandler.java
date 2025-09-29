package ufrn.imd.sources.workflows.chain;

public abstract class BaseChainHandler implements ChainHandler {
  private ChainHandler next;

  @Override
  public final void setNext(ChainHandler handler) {
   this.next = handler;
  };
  
  @Override
  public final String next(String prompt) {
    if(this.next != null) return this.next.handle(prompt);
    else return prompt;
  };
};
