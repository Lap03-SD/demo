package demo;

import basic.Card;
import basic.Rank;
import basic.Suit;
import hand.Hand;
import poker.PokerHandComparator;
import poker.PokerHandEvaluator;
import poker.PokerHandType;

public class Main {
    private static int aPassed = 0;
    private static int aFailed = 0;
    private static final PokerHandComparator CMP = new PokerHandComparator();

    /** Card from text: rank A,2-9,T,J,Q,K + suit C,D,H,S. Example: "KH", "TC". */
    private static Card card(String t) {
        Rank r = Rank.values()["A23456789TJQK".indexOf(t.charAt(0))];
        Suit s = Suit.values()["CDHS".indexOf(t.charAt(1))];
        return new Card(r, s);
    }

    /** Hand from text. Example: "AC 2H 3S 4C 5D". */
    private static Hand hand(String s) {
        Hand h = new Hand(5);
        for (String t : s.trim().split("\\s+")) h.add(card(t));
        return h;
    }

    private static void checkType(String cards, PokerHandType expected) {
        PokerHandType actual = PokerHandEvaluator.evaluate(hand(cards)).getType();
        report(cards + " is " + expected, actual == expected, "got " + actual);
    }

    /** expected: +1 if first wins, -1 if second wins, 0 if equal. */
    private static void checkCompare(String a, String b, int expected) {
        int actual = Integer.signum(CMP.compare(hand(a), hand(b)));
        String sym = expected > 0 ? ">" : expected < 0 ? "<" : "==";
        report("[" + a + "] " + sym + " [" + b + "]", actual == expected, "got " + actual);
    }

    private static void report(String what, boolean ok, String detail) {
        if (ok) aPassed++; else aFailed++;
        System.out.println((ok ? "PASS  " : "FAIL  ") + what + (ok ? "" : "   (" + detail + ")"));
    }

    public static void main(String[] args) {
        System.out.println("== Hand types ==");
        checkType("2C 4H 6S 8C KD", PokerHandType.HIGH_CARD);
        checkType("QC QH 3S 5C 8D", PokerHandType.ONE_PAIR);
        checkType("2C 2H 6S 6C TD", PokerHandType.TWO_PAIR);
        checkType("9C 9H 9S 2C 7D", PokerHandType.THREE_OF_A_KIND);
        checkType("AC 2H 3S 4C 5D", PokerHandType.STRAIGHT);          // ace low
        checkType("TC JH QS KC AD", PokerHandType.STRAIGHT);          // ace high
        checkType("QC KH AS 2C 3D", PokerHandType.HIGH_CARD);         // no wrap-around
        checkType("2H 5H 7H 9H JH", PokerHandType.FLUSH);
        checkType("KC KH KS 4C 4D", PokerHandType.FULL_HOUSE);
        checkType("8C 8H 8S 8D 2D", PokerHandType.FOUR_OF_A_KIND);
        checkType("5S 6S 7S 8S 9S", PokerHandType.STRAIGHT_FLUSH);
        checkType("TS JS QS KS AS", PokerHandType.STRAIGHT_FLUSH);    // royal flush

        System.out.println("\n== Different types ==");
        checkCompare("AC 2H 3S 4C 5D", "9C 9H 9S 2C 7D", 1);          // straight > trips
        checkCompare("2H 5H 7H 9H JH", "AC 2H 3S 4C 5D", 1);          // flush > straight
        checkCompare("TS JS QS KS AS", "8C 8H 8S 8D 2D", 1);          // straight flush > quads

        System.out.println("\n== Same type: tie-breaking ==");
        checkCompare("KC KH 5S 7C 9D", "2C 2H 5S 7D 9D", 1);          // pair K > pair 2
        checkCompare("KC KH 5S 7C 9D", "KD KS 5C 7H 8D", 1);          // same pair, kicker 9 > 8
        checkCompare("KC KH 2S 2C 9D", "QC QH JS JC AD", 1);          // top pair K > Q
        checkCompare("KC KH 3S 3C 9D", "KD KS 2C 2H AD", 1);          // same top pair, 3 > 2
        checkCompare("3C 3H 3S 2C 2D", "2C 2H 2S AC AD", 1);          // trips decide full house
        checkCompare("AC 9H 7S 5C 3D", "KC QH JS 9C 8D", 1);          // ace-high beats king-high
        checkCompare("TC JH QS KC AD", "AC 2H 3S 4C 5D", 1);          // ace-high straight > wheel
        checkCompare("6C 2H 3S 4C 5D", "AC 2H 3S 4C 5D", 1);          // 6-high > 5-high (wheel)
        checkCompare("KC KH 5S 7C 9D", "KD KS 5C 7H 9S", 0);          // truly equal

        System.out.println("\n== Hand without 5 cards ==");
        Hand small = new Hand(5);
        small.add(card("AC"));
        try {
            PokerHandEvaluator.evaluate(small);
            report("1-card hand rejected", false, "no exception");
        } catch (IllegalArgumentException e) {
            report("1-card hand rejected: " + e.getMessage(), true, "");
        }

        System.out.println("\nPassed: " + aPassed + ", Failed: " + aFailed);
    }
}