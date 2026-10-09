package hand;

import basic.Card;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** A hand of 0..N cards. */
public class Hand implements Iterable<Card> {
    private final List<Card> aCards = new ArrayList<>();
    private final int aMaxCards;

    /** @pre pMaxCards > 0 */
    public Hand(int pMaxCards) {
        assert pMaxCards > 0;
        aMaxCards = pMaxCards;
    }

    /** @pre pCard != null && !contains(pCard) && !isFull() */
    public void add(Card pCard) {
        assert pCard != null && !contains(pCard) && !isFull();
        aCards.add(pCard);
    }

    /** @pre pCard != null && contains(pCard) */
    public void remove(Card pCard) {
        assert pCard != null && contains(pCard);
        aCards.remove(pCard);
    }

    /** @pre pCard != null */
    public boolean contains(Card pCard) {
        assert pCard != null;
        return aCards.contains(pCard);
    }

    public boolean isEmpty() { return aCards.isEmpty(); }
    public int size() { return aCards.size(); }
    public boolean isFull() { return aCards.size() == aMaxCards; }

    @Override
    public Iterator<Card> iterator() {
        // Read-only view: clients cannot modify the hand through the iterator
        final Iterator<Card> it = aCards.iterator();
        return new Iterator<Card>() {
            @Override public boolean hasNext() { return it.hasNext(); }
            @Override public Card next() { return it.next(); }
        };
    }

    @Override
    public String toString() { return aCards.toString(); }
}