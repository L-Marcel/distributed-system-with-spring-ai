package ufrn.imd.sources.agents;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;

import ufrn.imd.sources.agents.summarizer.AFlow;
import ufrn.imd.sources.workflows.Workflow;

public class Summarizer extends Agent {
  @Autowired
  public Summarizer(ChatMemory memory) {
    super(memory);
  };
  
  public Integer execute(Float a) {
   return 
    Workflow.<Float, Integer>build()
      .chain(new AFlow())
      .run(a);
  };
};
