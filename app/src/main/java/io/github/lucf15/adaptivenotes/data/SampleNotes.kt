package io.github.lucf15.adaptivenotes.data

import io.github.lucf15.adaptivenotes.domain.model.Note
import io.github.lucf15.adaptivenotes.domain.model.NoteCategory

internal fun sampleNotes(): List<Note> {
    val bodies = listOf(
        "A slower kind of Sunday" to "No alarm. Coffee before messages. The long way home.\n\nI keep coming back to the idea that a good weekend needs a little less planning and a little more room. A walk without a destination. A book left open on the kitchen table. Lunch that quietly turns into the afternoon.\n\nA few things to make time for\n\nWalk down to the market early, while the streets are still quiet. Pick something for dinner without consulting a recipe. Stop at the little bakery on the corner.\n\nTake the camera, but leave the headphones behind. There are things I only notice when I am not listening to something else.\n\nFor later\n\nFind a place near the water. Order another coffee. Write down the things that have been taking up space in my head. Not a list of tasks. Just a few thoughts that deserve somewhere to land.\n\nAnd if none of this happens, that is fine too. The point was never to make a better plan. It was to leave a little space.",
        "Small things, well made" to "A collection of details I want to remember.\n\nThe gentle click of a good pen. A book that stays open on its own. A chair that makes you sit a little longer.\n\nThe best things do not need to explain themselves. They make an ordinary moment feel considered.\n\nFor the next project\n\nStart with one thing that matters. Give it enough attention. Remove what gets in the way.\n\nA useful question: does this make someone's day a little easier?",
        "A place by the water" to "Somewhere with open windows, a small kitchen, and no reason to hurry.\n\nBring a notebook. Find the morning swimming spot. Learn the name of the person who makes the coffee.\n\nA few possibilities\n\nA quiet town on the Adriatic. A little house with a terrace. A train journey that takes most of the afternoon.\n\nKeep the plans loose. Leave one whole day empty.",
        "The next chapter" to "Things to bring into the next project.\n\nA clear beginning. Fewer moving parts. More time spent using the thing we are making.\n\nMonday\n\nSketch the first screen on paper. Talk about what someone needs before talking about what we can build.\n\nBy the end of the week\n\nMake one complete journey work. Then use it, slowly, and pay attention to where it feels awkward.",
        "Books for the bedside" to "A small reading list, in no particular order.\n\nA Field Guide to Getting Lost — Rebecca Solnit\nThe Creative Act — Rick Rubin\nThe Book of Disquiet — Fernando Pessoa\n\nRead a few pages before reaching for the phone. Underline something. Let a book take as long as it needs.",
        "Dinner with friends" to "Saturday, around seven.\n\nTomatoes, good bread, something from the oven. Ask everyone to bring one thing.\n\nShopping\n\nLemons\nOlive oil\nFresh herbs\nA little more bread than seems necessary\n\nClear the table. Find the candles. Put on a record and let the evening happen.",
        "An idea for a rainy day" to "A little shelf for things picked up along the way.\n\nTrain tickets, postcards, a photograph from a walk. Objects that mean something without needing to be useful.\n\nStart with the small wooden frame in the cupboard. Leave room for the collection to change.",
        "On making space" to "A reminder to leave some breathing room.\n\nBetween meetings. Around the edges of a design. In the way we listen to each other.\n\nNot every gap needs filling. Sometimes the most useful thing to add is a pause.",
    )
    return bodies.mapIndexed { index, (title, body) ->
        Note("note-${index + 1}", title, body,
            listOf(NoteCategory.Personal, NoteCategory.Ideas, NoteCategory.Personal, NoteCategory.Work)[index % 4],
            index < 2, 1_795_603_200_000L - index * 86_400_000L)
    }
}
