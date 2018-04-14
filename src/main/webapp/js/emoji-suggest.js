function emojiDisplayer(err, suggestions) {

}

$(document).ready(() => {
  const userInput = $("#userInput");
  suggestBox(userInput, {
    ':': function (word, emojiDisplayer) {
        word = word.toLowerCase()
        var data = Object.keys(window)
                    .filter(function (k) {
                      return k.toLowerCase().indexOf(word) === 0
                    }).map(function (k) {
                      return {title: k, value: '.'+k}
                    })

        setTimeout(function () {
          cb(null, data)
        }, ~~(Math.random()*200))
      }
  });
});
