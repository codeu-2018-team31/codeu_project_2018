package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class TagTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    UUID conv_id1 = UUID.randomUUID();
    UUID conv_id2 = UUID.randomUUID();
    String tag = "test_tag";
    Instant creation = Instant.now();
    Conversation conversation1 = new Conversation(conv_id1, UUID.randomUUID(), "test", creation);
    Conversation conversation2 = new Conversation(conv_id2, UUID.randomUUID(), "test", creation);
    List<UUID> convo_ids = new ArrayList<>();
    convo_ids.add(conversation1.getId());
    convo_ids.add(conversation2.getId());

    Tag test_tag = new Tag(id, conversation1.getId(), tag, creation);
    test_tag.addConversation(conversation2.getId());

    Assert.assertEquals(id, test_tag.getId());
    Assert.assertEquals(conv_id1, test_tag.getConversation());
    Assert.assertEquals(convo_ids, test_tag.getConversations());
    Assert.assertEquals(tag, test_tag.getTag());
    Assert.assertEquals(creation, test_tag.getCreationTime());
  }
}

