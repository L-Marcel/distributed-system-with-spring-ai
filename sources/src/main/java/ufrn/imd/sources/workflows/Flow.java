package ufrn.imd.sources.workflows;

@FunctionalInterface
public interface Flow<Input, Output> {
  public Output handle(Input input);
};
