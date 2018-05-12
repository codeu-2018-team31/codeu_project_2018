package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    String tag = "test_tag";
    Instant creation = Instant.now();

    Tag test_tag = new Tag(id, tag, creation);

    Assert.assertEquals(id, test_tag.getId());
    Assert.assertEquals(tag, test_tag.getTag());
    Assert.assertEquals(creation, test_tag.getCreationTime());
  }
}
