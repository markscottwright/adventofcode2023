package adventofcode2023;

import java.util.HashMap;

import org.junit.Test;

import adventofcode2023.Day7.Card;
import adventofcode2023.Day7.Hand;
import adventofcode2023.Day7.HandType;
import adventofcode2023.Day7.HandWithJoker;

import static org.assertj.core.api.Assertions.assertThat;

public class Day7Test {

    @Test
    public void test() {
        assertThat(new Hand(Card.n2, Card.n3, Card.n4, Card.n5, Card.n6).handType)
                .isEqualTo(HandType.high_card);
        assertThat(new Hand(Card.n2, Card.n2, Card.n4, Card.n5, Card.n6).handType)
                .isEqualTo(HandType.one_pair);
        assertThat(new Hand(Card.n2, Card.n2, Card.n4, Card.n4, Card.n6).handType)
                .isEqualTo(HandType.two_pair);
        assertThat(new Hand(Card.n2, Card.n2, Card.n4, Card.n4, Card.n6).handType)
                .isEqualTo(HandType.two_pair);
        assertThat(new Hand(Card.n2, Card.n2, Card.n2, Card.n4, Card.n6).handType)
                .isEqualTo(HandType.three_of_a_kind);
        assertThat(new Hand(Card.n2, Card.n2, Card.n2, Card.n4, Card.n4).handType)
                .isEqualTo(HandType.full_house);
        assertThat(new Hand(Card.n2, Card.n2, Card.n2, Card.n2, Card.n6).handType)
                .isEqualTo(HandType.four_of_a_kind);
        assertThat(new Hand(Card.n2, Card.n2, Card.n2, Card.n2, Card.n2).handType)
                .isEqualTo(HandType.five_of_a_kind);

        assertThat(new Hand("AA8AA").handType).isEqualTo(HandType.four_of_a_kind);

        assertThat(Card.n2).isLessThan(Card.K);
    }

    @Test
    public void testComparisons() {
        assertThat(new Hand("33332")).isGreaterThan(new Hand("2AAAA"));
        assertThat(new Hand("77888")).isGreaterThan(new Hand("77788"));
    }

    String testData = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """;

    @Test
    public void testParsing() {
        HashMap<Hand, Integer> handToBid = Hand.parse(testData);
        
        long score = Hand.getScore(handToBid);
        assertThat(score).isEqualTo(6440);
    }
    
    @Test
    public void testPart2() throws Exception {
        HashMap<HandWithJoker, Integer> handToBid = HandWithJoker.parse(testData);
        long score = HandWithJoker.getScore(handToBid);
        assertThat(score).isEqualTo(5905);
    }

}
