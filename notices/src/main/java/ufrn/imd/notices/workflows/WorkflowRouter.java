package ufrn.imd.notices.workflows;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class WorkflowRouter<Input, Output, RouterOutput, Key> {
  private final Workflow<Input, Output> workflow;
  private final Function<Output, Key> selector;
  private final Map<Key, Workflow<Output, RouterOutput>> routes;

  protected WorkflowRouter(
    Workflow<Input, Output> workflow,
    Function<Output, Key> selector,
    Key key,
    Workflow<Output, RouterOutput> branch
  ) {
    this.workflow = workflow;
    this.selector = selector;
    this.routes = new LinkedHashMap<>();
  };

  public WorkflowRouter<Input, Output, RouterOutput, Key> route(
    Key key,
    Workflow<Output, RouterOutput> branch
  ) {
    this.routes.put(key, branch);
    return this;
  };

  public Workflow<Input, RouterOutput> otherwise(
    Workflow<Output, RouterOutput> otherwise
  ) {
    return new Workflow<>((Input input) -> {
      Output result = this.workflow.run(input);
      Key key = selector.apply(result);
      Workflow<Output, RouterOutput> selected = this.routes
        .getOrDefault(key, otherwise);
      
      return selected.run(result);
    });
  };
};