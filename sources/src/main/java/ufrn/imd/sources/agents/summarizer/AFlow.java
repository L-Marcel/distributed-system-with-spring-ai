package ufrn.imd.sources.agents.summarizer;

import ufrn.imd.sources.workflows.Flow;
import ufrn.imd.sources.workflows.Workflow;

public class AFlow implements Flow<Float, Integer> {
  @Override
  public <B, E> Workflow.Node<B, E, Float, Integer> node() {
    return new Workflow.Node<>();
  };

  @Override
  public Integer handle(Float input) {
    return Math.round(input);
  };
};
