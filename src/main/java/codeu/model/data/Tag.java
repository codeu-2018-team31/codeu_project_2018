package codeu.model.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Class representing a tag. */
public class Tag {
  private final UUID id;
  private final Conversation conversation;
  private final String tag;
  private final Instant creation;
  private List<Conversation> conversations;

  /**
   * Constructs a new Tag.
   *
   * @param id the ID of this Tag
   * @param conversation The first Conversation this Tag is associated with.
   * @param conversations the IDs of the Conversations this Tag belongs to
   * @param tag the content of this Tag
   * @param creation the creation time of this Tag
   */

  public Tag(UUID id, Conversation conversation, String tag, Instant creation) {

    this.id = id;
    this.conversation = conversation;
    this.tag = tag;
    this.creation = creation;
    this.conversations = new ArrayList<>();
    this.conversations.add(conversation);
  }

  /** Adds a Conversation that has this Tag */
  public void addConversation(Conversation conversation) {
    conversations.add(conversation);
  }

  /** Gets all Conversations that have this Tag */
  public List<Conversation> getConversations() {
    return conversations;
  }

  /** Gets the Conversation that this Tag was created with*/
  public Conversation getConversation() {
    return conversation;
  }

  /** Returns the ID of this Tag. */
  public UUID getId() {
    return id;
  }

<<<<<<< HEAD
=======
  /** Returns the ID of the Conversation this Tag belongs to. */
  public UUID getConversationId() {
    return conversation;
  }
>>>>>>> tagDatastore

  /** Returns the content of this Tag. */
  public String getTag() {
    return tag;
  }

  /** Returns the creation time of this Tag. */
  public Instant getCreationTime() {
    return creation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Tag)) {
      return false;
    }

    Tag other = (Tag) o;
    if (other.getTag().equals(tag)) {
      return true;
    }
    return false;
  }
}

