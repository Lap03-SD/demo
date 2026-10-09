package basic;

/** Immutable playing card. */
public final class Card {
    private final Rank aRank;
    private final Suit aSuit;

    /** @pre pRank != null && pSuit != null */
    public Card(Rank pRank, Suit pSuit) {
        assert pRank != null && pSuit != null;
        aRank = pRank;
        aSuit = pSuit;
    }

    public Rank getRank() { return aRank; }
    public Suit getSuit() { return aSuit; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card other = (Card) o;
        return aRank == other.aRank && aSuit == other.aSuit;
    }

    @Override
    public int hashCode() { return aRank.hashCode() * 31 + aSuit.hashCode(); }

    @Override
    public String toString() { return aRank + " of " + aSuit; }
}