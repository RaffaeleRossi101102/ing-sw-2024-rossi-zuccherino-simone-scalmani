//package SoftEng_2024.Model.Cards;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CardsPackageTesting {
//
//    // THIS TESTING CLASS NEEDS COMMENTS
//
//    @Test
//    void testResourceCardDeserialize() {
//        Queue<Card> resourceDeck = new LinkedList<>();
//        assertDoesNotThrow(() -> SoftEng_2024.Model.Cards.CardDeserializer.resourceCardDeserialize((List<Card>) resourceDeck));
//        assertFalse(resourceDeck.isEmpty());
//    }
//
//    @Test
//    void goldCardDeserialize() {
//        Queue<Card> goldDeck = new LinkedList<>();
//        assertDoesNotThrow(() -> SoftEng_2024.Model.Cards.CardDeserializer.resourceCardDeserialize((List<Card>) goldDeck));
//        assertFalse(goldDeck.isEmpty());
//    }
//
//    @Test
//    void starterCardDeserialize() {
//        Queue<Card> starterDeck = new LinkedList<>();
//        assertDoesNotThrow(() -> SoftEng_2024.Model.Cards.CardDeserializer.resourceCardDeserialize((List<Card>) starterDeck));
//        assertFalse(starterDeck.isEmpty());
//    }
//
//}