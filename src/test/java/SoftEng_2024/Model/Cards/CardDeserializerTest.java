package SoftEng_2024.Model.Cards;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static SoftEng_2024.Model.Cards.CardDeserializer.*;
import static SoftEng_2024.Model.Cards.CardDeserializer.resourceCardDeserialize;
import static org.junit.jupiter.api.Assertions.*;

class CardDeserializerTest {

    @Test
    void ResourceCardDeserialize() {
        Queue<Card> resourceDeck = new LinkedList<>();
        resourceCardDeserialize((List<Card>) resourceDeck);
        assertDoesNotThrow(() -> resourceCardDeserialize((List<Card>) resourceDeck));
        assertFalse(resourceDeck.isEmpty());
    }

    @Test
    void GoldCardDeserialize() {
        Queue<Card> goldDeck = new LinkedList<>();
        goldCardDeserialize((List<Card>) goldDeck);
        assertDoesNotThrow(() -> resourceCardDeserialize((List<Card>) goldDeck));
        assertFalse(goldDeck.isEmpty());
    }

    @Test
    void StarterCardDeserialize() {
        Queue<Card> starterDeck = new LinkedList<>();
        starterCardDeserialize((List<Card>) starterDeck);
        assertDoesNotThrow(() -> resourceCardDeserialize((List<Card>) starterDeck));
        assertFalse(starterDeck.isEmpty());
    }
}