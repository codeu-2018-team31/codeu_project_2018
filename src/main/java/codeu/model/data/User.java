// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final Instant creation;
  private final String hashedPassword;
  private String about;
  private Map<Tag, Set<Conversation>> taggedConversations;
  private Map<Conversation, Set<Tag>> conversationTags;

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param creation the creation time of this User
   * @param password the password of this User
   * @param about the about information of this User
   */
  public User(UUID id, String name, Instant creation, String hashedPassword, String about) {
    this.id = id;
    this.name = name;
    this.creation = creation;
    this.hashedPassword = hashedPassword;
    this.about = about;
    this.taggedConversations = new HashMap<>();
    this.conversationTags = new HashMap<>();
  }

  /**
   * Constructs a new User to be used during Registration. 
   * Includes default about information of an empty string.
   * @param id the ID of this User
   * @param name the username of this User
   * @param creation the creation time of this User
   * @param password the password of this User
   */
  public User(UUID id, String name, Instant creation, String hashedPassword) {
    this.id = id;
    this.name = name;
    this.creation = creation;
    this.hashedPassword = hashedPassword;
    this.about = "";
    this.taggedConversations = new HashMap<>();
    this.conversationTags = new HashMap<>();
  }

  /**
   * Adds a Tag to a Conversation.
   * @param tag The Tag of the Conversation.
   * @param conversation The Conversation to be tagged.
   */
  public void addTaggedConversation(Tag tag, Conversation conversation) {
    if (taggedConversations.containsKey(tag)) {
      taggedConversations.get(tag).add(conversation);
    } else {
      Set<Conversation> conversations = new HashSet<>();
      conversations.add(conversation);
      taggedConversations.put(tag, conversations);
    }
    if (conversationTags.containsKey(conversation)) {
      conversationTags.get(conversation).add(tag);
    } else {
      Set<Tag> tags = new HashSet<>();
      tags.add(tag);
      conversationTags.put(conversation, tags);
    }
  }

  /**
   * Gets an alphabetically sorted list of tags this User has added for a 
   * particular conversation.
   */
  public List<Tag> getConversationTags(Conversation conversation) {
    if (conversationTags.containsKey(conversation)) {
      List<Tag> listedTags = new ArrayList<>(conversationTags.get(conversation));
      listedTags.sort(new TagComparator());
      return listedTags;
    }
    return new ArrayList<>();
  }

  /**
   * Gets all the Conversations that this User has tagged.
   * @return A Map of Tags to Conversations with that Tag.
   */
  public Map<Tag, Set<Conversation>> getTaggedConversations() {
    return taggedConversations;
  }
  
  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the password of the User. */
  public String getPassword() {
    return hashedPassword;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  } 
}
