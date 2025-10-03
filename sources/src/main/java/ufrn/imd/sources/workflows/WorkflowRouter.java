package ufrn.imd.sources.workflows;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class WorkflowRouter {
  public static <Input, Key> RouteSelector<Input, Key> on(
    Function<Input, Key> selector
  ) {
    return new RouteSelector<>(selector);
  };

  public static class RouteSelector<Input, Key> {
    private final Function<Input, Key> selector;

    private RouteSelector(Function<Input, Key> selector) {
      this.selector = selector;
    };

    public <Output> RouterBuilder<Input, Output, Key> router(
      Workflow<Input, Output> defaultRoute
    ) {
      return new RouterBuilder<>(selector, defaultRoute);
    };
  };

  public static class RouterBuilder<Input, Output, Key> {
    private final Function<Input, Key> selector;
    private final Map<Key, Workflow<Input, Output>> routes;
    private Workflow<Input, Output> defaultRoute;

    RouterBuilder(
      Function<Input, Key> selector,
      Workflow<Input, Output> defaultRoute
    ) {
      this.selector = selector;
      this.defaultRoute = defaultRoute;
      this.routes = new LinkedHashMap<>();
    };

    public RouterBuilder<Input, Output, Key> route(
      Key key, 
      Workflow<Input, Output> branch
    ) {
      this.routes.put(key, branch);
      return this;
    };

    public Flow<Input, Output> build() {
      return (Input input) -> {
        Key key = selector.apply(input);
        Workflow<Input, Output> selected = this.routes
          .getOrDefault(key, this.defaultRoute);
        
        return selected.run(input);
      };
    };
  };
};