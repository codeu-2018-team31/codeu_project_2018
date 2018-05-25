package codeu.model.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Class representing a tag. */
public class Tag {
  private final UUID id;
  private final UUID conversationId;
  private final String tag;
  private final Instant creation;
  private List<UUID> conversations;

  /**
   * Constructs a new Tag.
   *
   * @param id the ID of this Tag
   * @param conversationId the Id of the Conversation this Tag is associated with.
   * @param tag the content of this Tag
   * @param creation the creation time of this Tag
   */

  public Tag(UUID id, UUID conversationId, String tag, Instant creation) {
    this.id = id;
    this.conversationId = conversationId;
    this.tag = tag;
    this.creation = creation;
  }

  /** Gets the Conversation that this Tag was created with*/
  public UUID getConversation() {
    return conversationId;
  }

  /** Returns the ID of this Tag. */
  public UUID getId() {
    return id;
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

