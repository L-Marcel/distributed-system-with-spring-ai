package ufrn.imd.notices.workflows;

import java.util.function.Function;

public final class WorkflowRouterSelector<Input, Output, Key> {
  private final Workflow<Input, Output> workflow;
  private final Function<Output, Key> selector;

  protected WorkflowRouterSelector(
    Workflow<Input, Output> workflow, 
    Function<Output, Key> selector
  ) {
    this.workflow = workflow;
    this.selector = selector;
  };

  public <NewOutput> WorkflowRouter<Input, Output, NewOutput, Key> route(
    Key key,
    Workflow<Output, NewOutput> branch
  ) {
    return new WorkflowRouter<>(this.workflow, this.selector, key, branch);
  };
};
