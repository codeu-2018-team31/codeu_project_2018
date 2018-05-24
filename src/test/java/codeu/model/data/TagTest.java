package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    UUID conv_id = UUID.randomUUID();
    String tag = "test_tag";
    Instant creation = Instant.now();
    Conversation conversation = new Conversation(UUID.randomUUID(), UUID.randomUUID(), "test", creation);

<<<<<<< HEAD
    Tag test_tag = new Tag(id, conv_id, tag, creation);
=======
    Tag test_tag = new Tag(id, conversation, tag, creation);
>>>>>>> 8eff34afbfa5875fec026b56da002eac05818858

    Assert.assertEquals(id, test_tag.getId());
    Assert.assertEquals(conv_id, test_tag.getConversationId());
    Assert.assertEquals(tag, test_tag.getTag());
    Assert.assertEquals(creation, test_tag.getCreationTime());
  }
}

