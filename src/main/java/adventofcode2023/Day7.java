package adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.bag.HashBag;

/**
 * Wow - having everything be strongly typed did *not* work out for part 2
 */
public class Day7 {
    public static enum Card {
        n2, n3, n4, n5, n6, n7, n8, n9, T, J, Q, K, A;

        static Card fromChar(char c) {
            switch (c) {
            case '2':
                return n2;
            case '3':
                return n3;
            case '4':
                return n4;
            case '5':
                return n5;
            case '6':
                return n6;
            case '7':
                return n7;
            case '8':
                return n8;
            case '9':
                return n9;
            case 'T':
                return T;
            case 'J':
                return J;
            case 'Q':
                return Q;
            case 'K':
                return K;
            case 'A':
                return A;
            }

            throw new RuntimeException("Bad letter " + c);
        }
    }

    public static enum CardWithJoker {
        J, n2, n3, n4, n5, n6, n7, n8, n9, T, Q, K, A;

        static CardWithJoker fromChar(char c) {
            switch (c) {
            case '2':
                return n2;
            case '3':
                return n3;
            case '4':
                return n4;
            case '5':
                return n5;
            case '6':
                return n6;
            case '7':
                return n7;
            case '8':
                return n8;
            case '9':
                return n9;
            case 'T':
                return T;
            case 'J':
                return J;
            case 'Q':
                return Q;
            case 'K':
                return K;
            case 'A':
                return A;
            }

            throw new RuntimeException("Bad letter " + c);
        }
    }

    public static enum HandType {
        high_card, one_pair, two_pair, three_of_a_kind, full_house, four_of_a_kind, five_of_a_kind
    }

    public static class Hand implements Comparable<Hand> {
        List<Card> cards;
        HandType handType;

        @Override
        public int hashCode() {
            return Objects.hash(cards);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Hand other = (Hand) obj;
            return Objects.equals(cards, other.cards);
        }

        public Hand(Card... cards) {
            this(List.of(cards));
        }

        public Hand(List<Card> cards) {
            this.cards = cards;

            HandType aHandType = determineHandType(cards);

            handType = aHandType;
        }

        private static HandType determineHandType(List<Card> cards) {
            HandType aHandType = null;
            HashBag<Card> counts = new HashBag<>(cards);
            if (counts.uniqueSet().size() == 5)
                aHandType = HandType.high_card;
            else if (counts.uniqueSet().size() == 4)
                aHandType = HandType.one_pair;
            else if (counts.uniqueSet().size() == 1)
                aHandType = HandType.five_of_a_kind;
            else if (counts.uniqueSet().size() == 2) {
                if (counts.stream().anyMatch(c -> counts.getCount(c) == 4)) {
                    aHandType = HandType.four_of_a_kind;
                } else {
                    aHandType = HandType.full_house;
                }
            } else if (counts.uniqueSet().size() == 3) {
                if (counts.stream().anyMatch(c -> counts.getCount(c) == 3)) {
                    aHandType = HandType.three_of_a_kind;
                } else {
                    aHandType = HandType.two_pair;
                }
            }
            return aHandType;
        }

        public Hand(String cards) {
            this(cards.chars().mapToObj(c -> Card.fromChar((char) c)).collect(Collectors.toList()));
        }

        @Override
        public int compareTo(Hand o) {
            int handComparison = handType.compareTo(o.handType);
            if (handComparison != 0)
                return handComparison;

            for (int i = 0; i < cards.size(); i++) {
                int cardComparison = cards.get(i).compareTo(o.cards.get(i));
                if (cardComparison != 0)
                    return cardComparison;
            }
            return 0;
        }

        public static HashMap<Hand, Integer> parse(String testData) {
            HashMap<Hand, Integer> out = new HashMap<>();
            Arrays.stream(testData.split("\n")).forEach(l -> {
                String[] fields = l.split("\\s+");
                out.put(new Hand(fields[0]), Integer.parseInt(fields[1]));
            });
            return out;
        }

        @Override
        public String toString() {
            return "Hand [cards=" + cards + ", handType=" + handType + "]";
        }

        static public long getScore(HashMap<Day7.Hand, Integer> handToBid) {
            ArrayList<Day7.Hand> hands = new ArrayList<>(handToBid.keySet());
            hands.sort(Day7.Hand::compareTo);

            long score = 0;
            for (int i = 0; i < hands.size(); ++i)
                score += (i + 1) * handToBid.get(hands.get(i));
            return score;
        }

    }

    public static class HandWithJoker implements Comparable<HandWithJoker> {
        List<CardWithJoker> cards;
        HandType handType;

        @Override
        public int hashCode() {
            return Objects.hash(cards);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            HandWithJoker other = (HandWithJoker) obj;
            return Objects.equals(cards, other.cards);
        }

        public HandWithJoker(CardWithJoker... cards) {
            this(List.of(cards));
        }

        public HandWithJoker(List<CardWithJoker> cards) {
            this.cards = cards;
            handType = determineHandTypeWithJokers(cards);
        }

        private HandType determineHandTypeWithJokers(List<CardWithJoker> cards) {
            HashBag<CardWithJoker> counts = new HashBag<>(cards);
            if (counts.getCount(CardWithJoker.J) == 0) {
                return determineHandTypeWithoutJokers(cards);
            } else if (counts.getCount(CardWithJoker.J) == 5) {
                return HandType.five_of_a_kind;
            } else if (counts.getCount(CardWithJoker.J) == 4) {
                return HandType.five_of_a_kind;
            } else if (counts.getCount(CardWithJoker.J) == 3) {
                if (counts.uniqueSet().size() == 2)
                    return HandType.five_of_a_kind;
                else
                    return HandType.four_of_a_kind;
            } else if (counts.getCount(CardWithJoker.J) == 1) {
                var temp = new ArrayList<CardWithJoker>(cards);
                int jokerPosition = cards.indexOf(CardWithJoker.J);
                return Arrays.stream(CardWithJoker.values()).skip(1).map(c -> {
                    temp.set(jokerPosition, c);
                    HandWithJoker handWithJoker = new HandWithJoker(temp);
                    return handWithJoker;
                }).max(HandWithJoker::compareTo).get().handType;
            } else { // counts.getCount(CardWithJoker.J) == 2
                var temp = new ArrayList<CardWithJoker>(cards);
                int joker1Position = temp.indexOf(CardWithJoker.J);
                int joker2Position = temp.lastIndexOf(CardWithJoker.J);
                HandWithJoker bestHand = null;
                for (CardWithJoker c1 : CardWithJoker.values()) {
                    if (c1 == CardWithJoker.J)
                        continue;
                    for (CardWithJoker c2 : CardWithJoker.values()) {
                        if (c2 == CardWithJoker.J)
                            continue;

                        temp.set(joker1Position, c1);
                        temp.set(joker2Position, c2);
                        HandWithJoker possibleHand = new HandWithJoker(temp);
                        if (bestHand == null)
                            bestHand = possibleHand;
                        else if (bestHand.compareTo(possibleHand) < 0)
                            bestHand = possibleHand;
                    }
                }
                if (bestHand == null)
                {
                    System.out.println(temp);
                    System.out.println(cards);
                    throw new RuntimeException();
                }
                return bestHand.handType;
            }
        }

        private HandType determineHandTypeWithoutJokers(List<CardWithJoker> cards) {
            HandType aHandType = null;
            HashBag<CardWithJoker> counts = new HashBag<>(cards);
            if (counts.uniqueSet().size() == 5)
                aHandType = HandType.high_card;
            else if (counts.uniqueSet().size() == 4)
                aHandType = HandType.one_pair;
            else if (counts.uniqueSet().size() == 1)
                aHandType = HandType.five_of_a_kind;
            else if (counts.uniqueSet().size() == 2) {
                if (counts.stream().anyMatch(c -> counts.getCount(c) == 4)) {
                    aHandType = HandType.four_of_a_kind;
                } else {
                    aHandType = HandType.full_house;
                }
            } else if (counts.uniqueSet().size() == 3) {
                if (counts.stream().anyMatch(c -> counts.getCount(c) == 3)) {
                    aHandType = HandType.three_of_a_kind;
                } else {
                    aHandType = HandType.two_pair;
                }
            }
            return aHandType;
        }

        public HandWithJoker(String cards) {
            this(cards.chars().mapToObj(c -> CardWithJoker.fromChar((char) c))
                    .collect(Collectors.toList()));
        }

        @Override
        public int compareTo(HandWithJoker o) {
            int handComparison = handType.compareTo(o.handType);
            if (handComparison != 0)
                return handComparison;

            for (int i = 0; i < cards.size(); i++) {
                int cardComparison = cards.get(i).compareTo(o.cards.get(i));
                if (cardComparison != 0)
                    return cardComparison;
            }
            return 0;
        }

        public static HashMap<HandWithJoker, Integer> parse(String testData) {
            HashMap<HandWithJoker, Integer> out = new HashMap<>();
            Arrays.stream(testData.split("\n")).forEach(l -> {
                String[] fields = l.split("\\s+");
                out.put(new HandWithJoker(fields[0]), Integer.parseInt(fields[1]));
            });
            return out;
        }

        @Override
        public String toString() {
            return "Hand [cards=" + cards + ", handType=" + handType + "]";
        }

        static public long getScore(HashMap<Day7.HandWithJoker, Integer> handToBid) {
            ArrayList<Day7.HandWithJoker> hands = new ArrayList<>(handToBid.keySet());
            hands.sort(Day7.HandWithJoker::compareTo);

            long score = 0;
            for (int i = 0; i < hands.size(); ++i)
                score += (i + 1) * handToBid.get(hands.get(i));
            return score;
        }

    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("testdata/day7.dat"));
        
        HashMap<Hand, Integer> handsToBids = Hand.parse(input);
        long part1 = Hand.getScore(handsToBids);
        System.out.println("day 7 part 1: " + part1);
        HashMap<HandWithJoker, Integer> handsToBids2 = HandWithJoker.parse(input);
        long part2 = HandWithJoker.getScore(handsToBids2);
        System.out.println("day 7 part 2: " + part2);
    }
}
