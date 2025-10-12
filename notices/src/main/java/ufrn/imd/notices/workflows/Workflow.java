package ufrn.imd.notices.workflows;

import java.util.function.Function;

// TODO - Comecei a fazer, me empolguei e vou acabar descartando

public final class Workflow<Input, Output> {
  private final Flow<Input, Output> flow;

  protected Workflow(Flow<Input, Output> flow) {
    this.flow = flow;
  };
  
  public static <Input, Output> Workflow<Input, Output> begin(
    Flow<Input, Output> flow
  ) {
    return new Workflow<>(flow);
  };

  public <NewOutput> Workflow<Input, NewOutput> chain(
    Flow<Output, NewOutput> next
  ) {
    return new Workflow<>((Input input) -> {
      Output result = this.flow.handle(input);
      return next.handle(result);
    });
  };

  public <Key> WorkflowRouterSelector<Input, Output, Key> router(
    Function<Output, Key> selector
  ) {
    return new WorkflowRouterSelector<>(this, selector);
  };

  public Output run(Input input) {
    return this.flow.handle(input);
  };
};