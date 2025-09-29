package ufrn.imd.sources.workflows;

public final class Workflow<B, E> {
  public static <B, E> Workflow<B, E> build() {
    return new Workflow<B, E>();
  };

  public <I, O> Node<B, E, I, O> chain(Flow<I, O> flow) {
    Node<B, E, I, O> root = new Workflow.Node<>();
    root.flow = flow;
    return root;
  };

  public static class Node<B, E, I, O>  {
    private Flow<I, O> flow;
    private Node<B, E, O, ?> next;
    private Node<B, E, B, ?> root;

    public <T> Node<B, E, O, T> chain(
      Flow<O, T> flow
    ) {
      if(this.root == null) 
        this.root = (Node<B, E, B, ?>) this;
      
      Node<B, E, O, T> node = flow.node();
      node.flow = flow;
      node.root = this.root;
      this.next = node;

      return node;
    };

    private E execute(I input) {
      O result = this.flow.handle(input);
      if(this.next == null) return (E) result;
      return this.next.execute(result);
    };

    public E run(B input) {
      return this.root.execute(input);
    };
  };
};
