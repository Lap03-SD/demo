package poker;

import basic.Card;
import basic.Rank;
import basic.Suit;
import hand.Hand;

import java.util.*;

public final class PokerHandEvaluator {
    private PokerHandEvaluator() {}

    /**
     * @pre pHand != null && pHand.size() == 5
     * @throws IllegalArgumentException if the precondition is violated
     */
    public static PokerHandValue evaluate(Hand pHand) {
        if (pHand == null || pHand.size() != 5) {
            throw new IllegalArgumentException("A poker hand must have exactly 5 cards");
        }

        final Map<Rank, Integer> counts = new EnumMap<>(Rank.class);
        Set<Suit> suits = EnumSet.noneOf(Suit.class);
        for (Card c : pHand) {
            counts.merge(c.getRank(), 1, Integer::sum);
            suits.add(c.getSuit());
        }

        // Ranks ordered by (number of cards desc, strength desc).
        // e.g. K-K-K-2-2 -> [K, 2]; 9-9-5-5-A -> [9, 5, A]
        List<Rank> ordered = new ArrayList<>(counts.keySet());
        ordered.sort((a, b) -> {
            int byCount = counts.get(b) - counts.get(a);
            return byCount != 0 ? byCount : strength(b) - strength(a);
        });

        List<Integer> pattern = new ArrayList<>();
        List<Integer> tieBreakers = new ArrayList<>();
        for (Rank r : ordered) {
            pattern.add(counts.get(r));
            tieBreakers.add(strength(r));
        }

        boolean flush = suits.size() == 1;
        boolean wheel = isWheel(counts);               // A-2-3-4-5
        boolean straight = wheel || isHighStraight(counts);

        if (straight) {
            // Only the top card matters; in the wheel the top card is the 5
            tieBreakers = List.of(wheel ? 5 : strength(ordered.get(0)));
        }

        PokerHandType type;
        if (straight && flush)   type = PokerHandType.STRAIGHT_FLUSH;
        else if (pattern.get(0) == 4) type = PokerHandType.FOUR_OF_A_KIND;
        else if (pattern.get(0) == 3 && pattern.get(1) == 2) type = PokerHandType.FULL_HOUSE;
        else if (flush)          type = PokerHandType.FLUSH;
        else if (straight)       type = PokerHandType.STRAIGHT;
        else if (pattern.get(0) == 3) type = PokerHandType.THREE_OF_A_KIND;
        else if (pattern.get(0) == 2 && pattern.get(1) == 2) type = PokerHandType.TWO_PAIR;
        else if (pattern.get(0) == 2) type = PokerHandType.ONE_PAIR;
        else                     type = PokerHandType.HIGH_CARD;

        return new PokerHandValue(type, tieBreakers);
    }

    /** Ace counts as the highest card (14), except inside the wheel. */
    private static int strength(Rank pRank) {
        return pRank == Rank.ACE ? 14 : pRank.ordinal() + 1;
    }

    /** A-2-3-4-5 */
    private static boolean isWheel(Map<Rank, Integer> pCounts) {
        return pCounts.keySet().equals(
                EnumSet.of(Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE));
    }

    /** 5 distinct ranks in sequence with Ace high: 2-3-4-5-6 ... 10-J-Q-K-A */
    private static boolean isHighStraight(Map<Rank, Integer> pCounts) {
        if (pCounts.size() != 5) return false;
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (Rank r : pCounts.keySet()) {
            min = Math.min(min, strength(r));
            max = Math.max(max, strength(r));
        }
        return max - min == 4;
    }
}