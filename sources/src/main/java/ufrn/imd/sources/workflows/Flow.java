package ufrn.imd.sources.workflows;

public interface Flow<I, O> {
  public <B, E> Workflow.Node<B, E, I, O> node();
  public O handle(I input);
};
