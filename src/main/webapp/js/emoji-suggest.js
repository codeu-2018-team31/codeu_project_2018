$(document).ready(() => {
  const userInput = $("#userInput");
  const editor = new Textcomplete.editors.Textarea(userInput[0]);
  const textcomplete = new Textcomplete(editor, {
    dropdown: {
      maxCount: 5,
      placement: 'top'
    }
  });
  // Initialize Emoji convertor
  const emoji = new EmojiConvertor();
  emoji.init_env();
  // Initialize colon-to-emoji
  emoji.replace_colons("");
  emoji_names = [];
  for (let key in emoji.map.colons) {
    emoji_names.push(key + emoji.replace_colons(":" + key + ":"));
  }

  textcomplete.register([{
    // Emoji strategy
    match: /(^|\s):(\w+)$/,
    search: function (term, callback) {
      // Filters the suggestions list for ones that start with the currently
      // typed term
      callback(emoji_names.filter(emoji => { return emoji.startsWith(term); }).sort());
    },
    replace: function (value) {
      // Remove unicode characters from the value (in the form <short_name><Unicode emoji>)
      const short_name = ":" + value.replace(/[^\x00-\x7F]/g, "") + ":";
      console.log(short_name);
      console.log(emoji.replace_colons(short_name));

      // Replace the matched phrase with the clicked suggestion's emoji
      return emoji.replace_colons(short_name) + " ";
    }
  }]);
});
