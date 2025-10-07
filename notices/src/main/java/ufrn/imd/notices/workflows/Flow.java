package ufrn.imd.notices.workflows;

@FunctionalInterface
public interface Flow<Input, Output> {
  public Output handle(Input input);
};
