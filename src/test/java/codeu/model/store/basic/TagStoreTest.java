package codeu.model.store.basic;

import codeu.model.data.Tag;
import codeu.model.data.Conversation;
import codeu.model.store.persistence.PersistentStorageAgent;

import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TagStoreTest {

  private TagStore tagStore;
  private PersistentStorageAgent mockPersistentStorageAgent;
  Conversation conversationOne = 
      new Conversation(UUID.randomUUID(), UUID.randomUUID(), "conversationOne", Instant.now());
  Conversation conversationTwo = 
      new Conversation(UUID.randomUUID(), UUID.randomUUID(), "conversationTwo", Instant.now());

  private final Tag TAG_ONE =
      new Tag(
          UUID.randomUUID(), conversationOne.getId(), "tag_one", Instant.ofEpochMilli(1000));
  private final Tag TAG_TWO =
      new Tag(
          UUID.randomUUID(), conversationOne.getId(), "tag_two", Instant.ofEpochMilli(2000));
  private final Tag TAG_THREE =
      new Tag(
          UUID.randomUUID(), conversationTwo.getId(), "tag_three", Instant.ofEpochMilli(3000));
  

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    tagStore = TagStore.getTestInstance(mockPersistentStorageAgent);

    final List<Tag> tagList = new ArrayList<>();
    tagList.add(TAG_ONE);
    tagList.add(TAG_TWO);
    tagList.add(TAG_THREE);
    tagStore.setTags(new HashSet<>(tagList));
  }

  @Test
  public void testGetTagsInConversation() {
    Set<Tag> resultTags = tagStore.getTagsInConversation(conversationOne.getId());

    Assert.assertEquals(2, resultTags.size());
    assertTrue(resultTags.contains(TAG_ONE));
    assertTrue(resultTags.contains(TAG_TWO));
  } 

  @Test
  public void testAddTag() {
    Conversation inputConversation = 
      new Conversation(UUID.randomUUID(), UUID.randomUUID(), "conversationOne", Instant.now());
    Tag inputTag =
        new Tag(UUID.randomUUID(), inputConversation.getId(), "test_tag", Instant.now());
      tagStore.addTag(inputTag);
    Tag resultTag =
        tagStore.getTagsInConversation(inputConversation.getId()).iterator().next();

    assertEquals(inputTag, resultTag);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputTag);
  }

  private void assertEquals(Tag expectedTag, Tag actualTag) {
    Assert.assertEquals(expectedTag.getId(), actualTag.getId());
    Assert.assertEquals(expectedTag.getConversation(), actualTag.getConversation());
    Assert.assertEquals(expectedTag.getTag(), actualTag.getTag());
    Assert.assertEquals(expectedTag.getCreationTime(), actualTag.getCreationTime());
  }
}

