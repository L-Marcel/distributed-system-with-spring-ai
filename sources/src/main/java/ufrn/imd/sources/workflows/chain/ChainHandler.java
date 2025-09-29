package ufrn.imd.sources.workflows.chain;

public interface ChainHandler {
  void setNext(ChainHandler handler);
  public String handle(String prompt);
  public String next(String prompt);
};
