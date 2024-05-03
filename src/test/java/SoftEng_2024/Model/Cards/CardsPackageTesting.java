package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;
import SoftEng_2024.Model.Fronts.GoldFront;
import SoftEng_2024.Model.Fronts.ResourceFront;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static SoftEng_2024.Model.Cards.CardDeserializer.resourceCardDeserialize;
import static org.junit.jupiter.api.Assertions.*;

class CardsPackageTesting {

    // THIS TESTING CLASS NEEDS COMMENTS

    @Test
    void ResourceCardDeserialize() {
        Queue<Card> resourceDeck = new LinkedList<>();
        assertDoesNotThrow(() -> resourceCardDeserialize((List<Card>) resourceDeck));
        assertFalse(resourceDeck.isEmpty());
    }

    @Test
    void GoldCardDeserialize() {
        Queue<Card> goldDeck = new LinkedList<>();
        assertDoesNotThrow(() -> resourceCardDeserialize((List<Card>) goldDeck));
        assertFalse(goldDeck.isEmpty());
    }

    @Test
    void StarterCardDeserialize() {
        Queue<Card> starterDeck = new LinkedList<>();
        assertDoesNotThrow(() -> resourceCardDeserialize((List<Card>) starterDeck));
        assertFalse(starterDeck.isEmpty());
    }

    @Test
    void ResourcePrintableCardStringFlipped() {
        boolean flipped = true;
        ResourceFront printableFront = new ResourceFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.ANIMALS}, 0, new boolean[]{false, false, false, false});
        ResourceCard printableCard = new ResourceCard(printableFront, flipped, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.ANIMALS});
        String printableCardString = printableCard.getPrintableCardString(flipped);
        assertEquals(String.format("%s resource card on its back with empty visible angles and a visible %s in the center", printableCard.getResources()[4], printableCard.getResources()[4]), printableCardString);
    }

    @Test
    void ResourcePrintableCardStringNotFlipped() {
        boolean flipped = false;
        ResourceFront printableFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.EMPTY, Angles.EMPTY, Angles.ANIMALS}, 0, new boolean[]{false, false, false, false});
        ResourceCard printableCard = new ResourceCard(printableFront, flipped, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.ANIMALS});
        String printableCardString = printableCard.getPrintableCardString(flipped);
        assertEquals(String.format("%s resource card on its front with these type of angles: %s %s %s %s\n" +
                "and nothing in the center", printableCard.getResources()[4], printableCard.getFront().getFrontAngles()[0], printableCard.getFront().getFrontAngles()[1], printableCard.getFront().getFrontAngles()[2], printableCard.getFront().getFrontAngles()[3]), printableCardString);
    }

    @Test
    void StarterPrintableCardStringFlipped() {
        boolean flipped = true;
        ResourceFront printableFront = new ResourceFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        StarterCard printableCard = new StarterCard(printableFront, flipped, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
        String printableCardString = printableCard.getPrintableCardString(flipped);
        assertEquals(String.format("starter card on its back with these type of angles:\n" +
                "%s %s %s %s \n" +
                "and nothing in the center", printableCard.getResources()[0], printableCard.getResources()[1], printableCard.getResources()[2], printableCard.getResources()[3]), printableCardString);
    }

    @Test
    void StarterPrintableCardStringNotFlipped() {
        boolean flipped = false;
        ResourceFront printableFront = new ResourceFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        StarterCard printableCard = new StarterCard(printableFront, flipped, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
        String printableCardString = printableCard.getPrintableCardString(flipped);
        assertEquals(String.format("starter card on its front with these type of angles:%s %s %s %s\n" +
                "and these resources in the center:\n" +
                "%s %s %s", printableCard.getFront().getFrontAngles()[0], printableCard.getFront().getFrontAngles()[1], printableCard.getFront().getFrontAngles()[2], printableCard.getFront().getFrontAngles()[3], printableCard.getFront().getFrontAngles()[4], printableCard.getFront().getFrontAngles()[5], printableCard.getFront().getFrontAngles()[6]), printableCardString);
    }

    @Test
    void GoldPrintableCardStringFlipped() {
        boolean flipped = true;
        GoldFront printableFront = new GoldFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false}, ScoreTypes.STATIC, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
        GoldCard printableCard = new GoldCard(printableFront, flipped, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
        String printableCardString = printableCard.getPrintableCardString(flipped);
        assertEquals(String.format("%s gold card on its back with empty visible angles, no required resources to place and a visible %s in the center",
                printableCard.getResources()[4], printableCard.getResources()[4]), printableCardString);
    }

//    @Test
//    void GoldPrintableCardStringNotFlipped() {
//        boolean flipped = false;
//        GoldFront printableFront = new GoldFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false}, ScoreTypes.STATIC, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
//        GoldCard printableCard = new GoldCard(printableFront, flipped, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
//        String printableCardString = printableCard.getPrintableCardString(flipped);
//        assertEquals(String.format("%s gold card on its front with these required resources: %s:%s %s:%s %s:%s %s:%s\n" +
//                ",these type of angles: %s %s %s %s\n" +
//                "and nothing in the center", printableCard.getResources()[4], /*mancano le risorse ma funge*/ , printableCard.getFront().getFrontAngles()[0], printableCard.getFront().getFrontAngles()[1], printableCard.getFront().getFrontAngles()[2], printableCard.getFront().getFrontAngles()[3]), printableCardString);
//    }
}