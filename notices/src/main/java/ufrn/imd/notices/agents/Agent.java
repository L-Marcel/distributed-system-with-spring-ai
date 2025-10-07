package ufrn.imd.notices.agents;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;

@Getter
public abstract class Agent {
  private ChatMemory memory;
  
  @Autowired
  public Agent(
    ChatMemory memory
  ) {
    this.memory = memory;
  };
};
