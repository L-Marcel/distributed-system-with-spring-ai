package ufrn.imd.sources.agents;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;

import ufrn.imd.sources.workflows.Workflow;

public class Summarizer extends Agent {
  @Autowired
  public Summarizer(ChatMemory memory) {
    super(memory);
  };
  
  public String execute(Float a) {
    return Workflow
      .begin((Float i) -> Integer.parseInt(i.toString()))
      .chain((Integer b) -> b.toString())
      .chain((e) -> e)
      .run(a);
  };
};
