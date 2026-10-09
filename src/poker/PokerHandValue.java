package poker;

import java.util.List;

/**
 * The full strength of a poker hand: its type plus the tie-breaking
 * rank values (most important first).
 */
public final class PokerHandValue implements Comparable<PokerHandValue> {
    private final PokerHandType aType;
    private final List<Integer> aTieBreakers;

    PokerHandValue(PokerHandType pType, List<Integer> pTieBreakers) {
        aType = pType;
        aTieBreakers = List.copyOf(pTieBreakers);
    }

    public PokerHandType getType() { return aType; }
    public List<Integer> getTieBreakers() { return aTieBreakers; }

    @Override
    public int compareTo(PokerHandValue pOther) {
        int result = aType.compareTo(pOther.aType);
        if (result != 0) return result;
        for (int i = 0; i < aTieBreakers.size() && i < pOther.aTieBreakers.size(); i++) {
            result = Integer.compare(aTieBreakers.get(i), pOther.aTieBreakers.get(i));
            if (result != 0) return result;
        }
        return 0;
    }

    @Override
    public String toString() { return aType + " " + aTieBreakers; }
}