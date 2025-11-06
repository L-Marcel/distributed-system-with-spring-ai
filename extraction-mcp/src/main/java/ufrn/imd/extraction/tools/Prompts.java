package ufrn.imd.extraction.tools;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import ufrn.imd.extraction.errors.InternalPromptNotFound;

@Component
public final class Prompts {
  private ResourcePatternResolver resolver;
  private Map<String, Resource> prompts;

  @Autowired
  public Prompts(
    ResourcePatternResolver resolver
  ) {
    this.resolver = resolver;
    this.prompts = new LinkedHashMap<>();
  };
  
  @PostConstruct
  public void init() throws IOException {
    Resource[] prompts = this.resolver.getResources(
      "classpath:prompts/*.*"
    );

    for(Resource prompt : prompts) {
      this.prompts.put(
        StringUtils.stripFilenameExtension(
          prompt.getFilename()
        ),
        prompt
      );
    };
  };

  public Resource get(String key) {
    if(!this.prompts.containsKey(key))
      throw new InternalPromptNotFound();
    return this.prompts.get(key);
  };
};
