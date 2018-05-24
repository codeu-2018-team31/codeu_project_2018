package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    String tag = "test_tag";
    Instant creation = Instant.now();
    Conversation conversation = new Conversation(UUID.randomUUID(), UUID.randomUUID(), "test", creation);

    Tag test_tag = new Tag(id, conversation, tag, creation);

    Assert.assertEquals(id, test_tag.getId());
    Assert.assertEquals(tag, test_tag.getTag());
    Assert.assertEquals(creation, test_tag.getCreationTime());
  }
}

