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

package codeu.model.store.basic;

import codeu.model.data.Tag;
import codeu.model.data.Conversation;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */

public class TagStore {

  /** Singleton instance of TagStore. */
  private static TagStore instance;

  /**
   * Returns the singleton instance of TagStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static TagStore getInstance() {
    if (instance == null) {
      instance = new TagStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static TagStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new TagStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Tags from and saving Tags
   * to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Tags. */
  private List<Tag> tags;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private TagStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    tags = new ArrayList<>();
  }

  /**
   * Load a set of randomly-generated Tag objects.
   *
   * @return false if a error occurs.
   */
  public boolean loadTestData() {
    boolean loaded = false;
    try {
      tags.addAll(DefaultDataStore.getInstance().getAllTags());
      loaded = true;
    } catch (Exception e) {
      loaded = false;
      System.err.println("ERROR: Unable to establish initial store (tags).");
    }
    return loaded;
  }

  /**
   * Access the Tag object with the given name.
   *
   * @return null if Tag name does not match any existing Tag.
   */
  public Tag getTag(String tag_contents) {
    // This approach will be pretty slow if we have many tags.
    for (Tag t : tags) {
      if (tag_contents.equals(t.getTag())) {
        return t;
      }
    }
    return null;
  }

  /** Access the current set of tags known to the application. */
  public List<Tag> getAllTags() {
    return tags;
  }

  /** Add a new Tag to the current set of tags known to the application. */
  public void addTag(Tag tag) {
    tags.add(tag);
    persistentStorageAgent.writeThrough(tag);
  }

  /** Access the current set of Tags within the given Conversation. */
  public List<Tag> getTagsInConversation(UUID conversationId) {

    List<Tag> tagsInConversation = new ArrayList<>();

    for (Tag tag : tags) {
      if (tag.getConversation().equals(conversationId)) {
        tagsInConversation.add(tag);
      }
    }

    return tagsInConversation;
  }

  /** Access the current set of Conversations within the given Tag. */
  public List<UUID> getConversationsInTag(Tag tag) {
    return tag.getConversations();
  }

   /** Check whether a Conversation title is already known to the application. */
  public boolean isTagTaken(String tag) {
    // This approach will be pretty slow if we have many Conversations.
    for (Tag t : tags) {
      if (tag.equals(t.getTag())) {
        return true;
      }
    }
    return false;
  }

  /** Writes through the current set of tags known to the application. 
    * To be called when updating a Tag's attribute.  
    */
  public void putTag(Tag tag) {
    persistentStorageAgent.writeThrough(tag);
  }

  /** Sets the List of Tags stored by this TagStore. */
  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
}
