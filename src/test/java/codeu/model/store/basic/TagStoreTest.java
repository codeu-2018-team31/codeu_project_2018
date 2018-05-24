package codeu.model.store.basic;

import codeu.model.data.Tag;
import codeu.model.data.Conversation;
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
  Conversation conversationOne = 
      new Conversation(UUID.randomUUID(), UUID.randomUUID(), "conversationOne", Instant.now());
  Conversation conversationTwo = 
      new Conversation(UUID.randomUUID(), UUID.randomUUID(), "conversationTwo", Instant.now());

  private final Tag TAG_ONE =
      new Tag(
          UUID.randomUUID(), conversationOne, "tag_one", Instant.ofEpochMilli(1000));
  private final Tag TAG_TWO =
      new Tag(
          UUID.randomUUID(), conversationOne, "tag_two", Instant.ofEpochMilli(2000));
  private final Tag TAG_THREE =
      new Tag(
          UUID.randomUUID(), conversationTwo, "tag_three", Instant.ofEpochMilli(3000));
  TAG_ONE.addConversation(conversationTwo);


  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    tagStore = TagStore.getTestInstance(mockPersistentStorageAgent);

    final List<Tag> tagList = new ArrayList<>();
    tagList.add(TAG_ONE);
    tagList.add(TAG_TWO);
    tagList.add(TAG_THREE);
    tagStore.setTags(tagList);
  }

  @Test
  public void testGetTagsInConversation() {
    List<Tag> resultTags = tagStore.getTagsInConversation(CONVERSATION_ID_ONE);

    Assert.assertEquals(2, resultTags.size());
    assertEquals(TAG_ONE, resultTags.get(0));
    assertEquals(TAG_TWO, resultTags.get(1));
  }

   @Test
  public void testgetConversationsInTag() {
    List<Conversation> resultConversations = tagStore.getConversationsInTag(TAG_ONE);

    Assert.assertEquals(2, resultConversations.size());
    assertEquals(conversationOne, resultConversations.get(0));
    assertEquals(conversationTwo, resultConversations.get(1));
  }

  @Test
  public void testgetConversationsInTag2() {
    List<Conversation> resultConversations = tagStore.getConversationsInTag(TAG_TWO);

    Assert.assertEquals(1, resultConversations.size());
    assertEquals(conversationOne, resultConversations.get(0));
  }

  @Test
  public void testAddTag() {
    Conversation inputConversation = 
      new Conversation(UUID.randomUUID(), UUID.randomUUID(), "conversationOne", Instant.now());
    Tag inputTag =
        new Tag(UUID.randomUUID(), inputConversation, "test_tag", Instant.now());
      tagStore.addTag(inputTag);
    Tag resultTag =
        tagStore.getTagsInConversation(inputConversation).get(0);

    assertEquals(inputTag, resultTag);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputTag);
  }

  private void assertEquals(Tag expectedTag, Tag actualTag) {
    Assert.assertEquals(expectedTag.getId(), actualTag.getId());
    Assert.assertEquals(expectedTag.getConversation().getId(), actualTag.getConversation().getId());
    Assert.assertEquals(expectedTag.getTag(), actualTag.getTag());
    Assert.assertEquals(expectedTag.getCreationTime(), actualTag.getCreationTime());
  }
}

