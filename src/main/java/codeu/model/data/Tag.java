package codeu.model.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Class representing a tag. */
public class Tag {
  private final UUID id;
  private final String tag;
  private final UUID conversation;
  private final List<UUID> conversations;
  private final Instant creation;
  private List<Conversation> conversations;

  /**
   * Constructs a new Tag.
   *
   * @param id the ID of this Tag
<<<<<<< HEAD
   * @param conversations the IDs of the Conversations this Tag belongs to
   * @param tag the content of this Tag
   * @param creation the creation time of this Tag
   */
=======
   * @param conversation The first Conversation this Tag is associated with.
   * @param tag the content of this Tag
   * @param creation the creation time of this Tag
   */
  public Tag(UUID id, Conversation conversation, String tag, Instant creation) {

    this.id = id;
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

  /** Returns the ID of this Tag. */
  public UUID getId() {
    return id;
  }

  /** Returns the List of Conversation IDs this tag belongs to. */
  public List<UUID> getConversationIds() {
    return conversations;
  }

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

