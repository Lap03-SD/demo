package poker;

import hand.Hand;

import java.util.Comparator;

/** Compares hands by poker strength: type first, then tie-breaking ranks. */
public class PokerHandComparator implements Comparator<Hand> {
    @Override
    public int compare(Hand pHand1, Hand pHand2) {
        return PokerHandEvaluator.evaluate(pHand1)
                .compareTo(PokerHandEvaluator.evaluate(pHand2));
    }
}