package net.kingchev

import net.kingchev.game.model.Card
import net.kingchev.game.model.Deck
import net.kingchev.game.model.Suit
import net.kingchev.game.model.deck
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DeckTest {
    @Test
    fun deckCreationTest() {
        val exceptedDeck = deck {
            card { suit(Suit.CLUBS);denomination(1);trump(false) }
            card { suit(Suit.HEARTS);denomination(2);trump(true) }
            card { suit(Suit.DIAMONDS); denomination(3); trump(false) }
            card { suit(Suit.SPADES);denomination(4);trump(true) }
        }

        val actualDeck = Deck(
            hashSetOf(
                Card(Suit.CLUBS, denomination = 1, isTrump = false),
                Card(Suit.HEARTS, denomination = 2, isTrump = true),
                Card(Suit.DIAMONDS, denomination = 3, isTrump = false),
                Card(Suit.SPADES, denomination = 4, isTrump = true)
            )
        )

        assertEquals(exceptedDeck, actualDeck)
    }
}