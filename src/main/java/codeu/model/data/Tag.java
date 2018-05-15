package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

/** Class representing a tag. */
public class Tag {
  private final UUID id;
  private final String tag;
  private final Instant creation;

  /**
   * Constructs a new Tag.
   *
   * @param id the ID of this Tag
   * @param tag the content of this Tag
   * @param creation the creation time of this Tag
   */
  public Tag(UUID id, String tag, Instant creation) {
    this.id = id;
    this.tag = tag;
    this.creation = creation;
  }

  /** Returns the ID of this Tag. */
  public UUID getId() {
    return id;
  }

  /** Returns the content of this Tag. */
  public String getTag() {
    return tag;
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }
}

