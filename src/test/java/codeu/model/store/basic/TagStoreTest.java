package codeu.model.store.basic;

import codeu.model.data.Tag;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TagStoreTest {

  private TagStore tagStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final Tag TAG_ONE =
      new Tag(
          UUID.randomUUID(), "tag_one", Instant.ofEpochMilli(1000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    tagStore = TagStore.getTestInstance(mockPersistentStorageAgent);

    final List<Tag> tagList = new ArrayList<>();
    tagList.add(TAG_ONE);
    tagStore.setTags(tagList);
  }

  

  @Test
  public void testAddTag() {
    UUID inputConversationId = UUID.randomUUID();
    Tag inputTag =
        new Tag(UUID.randomUUID(), inputConversationId, "test_tag", Instant.now());
      tagStore.addTag(inputTag);
    Tag resultTag =
        tagStore.getTagsInConversation(inputConversationId).get(0);

    assertEquals(inputTag, resultTag);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputTag);
  }

  private void assertEquals(Tag expectedTag, Tag actualTag) {
    Assert.assertEquals(expectedTag.getId(), actualTag.getId());
    Assert.assertEquals(expectedTag.getConversationId(), actualTag.getConversationId());
    Assert.assertEquals(expectedTag.getTag(), actualTag.getTag());
    Assert.assertEquals(expectedTag.getCreationTime(), actualTag.getCreationTime());
  }
}
