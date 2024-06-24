package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;
import SoftEng_2024.Model.Fronts.Front;
import SoftEng_2024.Model.Fronts.GoldFront;
import SoftEng_2024.Model.Fronts.ResourceFront;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CardDeserializer {

    // USED FOR DEBUGGING - Main method to run the deserializers - decks of cards are instantiated inside the main method and then passed to the corresponding deserialization function
    // We use queues as they are equipped with the pop logic which helps during the game - working as a "draw" action. The only downside is that when the game is starting
    // we have to temporarily convert the queues into lists in order to run a shuffle method and have our decks shuffled in a random order, and then re-convert the list
    // into a queue in order to get back the possibility to use the pop functionality


    // Method to deserialize the .json file containing the resource cards. Basically, parsing only the useful info
    // from the .json file (e.g. angles and the back resource), then, creating the card with the infos stored in the
    // resourceCardsInfoArray variable - repeat the process for both the gold and starter cards, paying attention to modify
    // the corresponding .json files according to how the support card classes are made. Comments included for the resource
    // deserializer, as the workflow is identical for the most part between the three methods.
    public static void resourceCardDeserialize(List<Card> resourceDeck) {

        // Adding a new gson instance
        Gson gson = new Gson();

        // Reading from a file needs an exception in case there is a memory fault for not finding the file
        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/SoftEng_2024/Model/Cards/resourceCards.json"));
            SupportResourceCard[] resourceCardsInfoArray = gson.fromJson(reader, SupportResourceCard[].class);

            boolean flipped = false;
            for (SupportResourceCard card : resourceCardsInfoArray) {
                boolean[] covered = new boolean[4];
                Arrays.fill(covered, false);
                Angles[] frontAnglesTemp = new Angles[4];
                Angles[] resourcesTemp = new Angles[5];
                Arrays.fill(resourcesTemp, Angles.EMPTY);
                for (int j = 0; j < 4; j++) {
                    frontAnglesTemp[j] = Angles.valueOf(card.angles[j]);
                }
                resourcesTemp[4] = Angles.valueOf(card.backResource);
                Front resourceFront = new ResourceFront(frontAnglesTemp, card.points, covered);
                Card resourceCard = new ResourceCard(resourceFront, flipped, resourcesTemp, card.cardID);
                resourceDeck.add(resourceCard);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Separate method for deserializing gold cards, from a different .json file
    public static void goldCardDeserialize(List<Card> goldDeck) {

        // Adding a new gson instance
        Gson gson = new Gson();

        // Reading from a file needs an exception in case there is a memory fault for not finding the file
        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/SoftEng_2024/Model/Cards/goldCards.json"));
            SupportGoldCard[] goldCardsInfoArray = gson.fromJson(reader, SupportGoldCard[].class);

            boolean flipped = false;
            for (SupportGoldCard card : goldCardsInfoArray) {
                boolean[] covered = new boolean[4];
                Arrays.fill(covered, false);
                Angles[] frontAnglesTemp = new Angles[4];
                Angles[] resourcesTemp = new Angles[5];
                Angles[] requiredResourcesTemp = new Angles[5];
                Arrays.fill(resourcesTemp, Angles.EMPTY);
                for (int j = 0; j < 4; j++) {
                    frontAnglesTemp[j] = Angles.valueOf(card.angles[j]);
                }
                for (int j = 0; j < 5; j++) {
                    requiredResourcesTemp[j] = Angles.valueOf(card.requiredResources[j]);
                }
                String scoreTypeTemp = card.scoreType;
                resourcesTemp[4] = Angles.valueOf(card.backResource);
                Front goldFront = new GoldFront(frontAnglesTemp, card.points, covered, ScoreTypes.valueOf(scoreTypeTemp), requiredResourcesTemp);
                Card goldCard = new GoldCard(goldFront, flipped, resourcesTemp, card.cardID);
                goldDeck.add(goldCard);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void starterCardDeserialize(List<Card> starterDeck) {

        // Adding a new gson instance
        Gson gson = new Gson();

        // Reading from a file needs an exception in case there is a memory fault for not finding the file
        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/SoftEng_2024/Model/Cards/starterCards.json"));
            SupportStarterCard[] starterCardsInfoArray = gson.fromJson(reader, SupportStarterCard[].class);
            boolean flipped = false;

            int points = 0;

            for (SupportStarterCard card : starterCardsInfoArray) {
                boolean[] covered = new boolean[4];
                Arrays.fill(covered, false);
                Angles[] startingResourcesTemp = new Angles[7];
                Angles[] startingBackResourcesTemp = new Angles[5];
                for (int j = 0; j < 7; j++) {
                    startingResourcesTemp[j] = Angles.valueOf(card.angles[j]);
                }
                for (int j = 0; j < 5; j++) {
                    startingBackResourcesTemp[j] = Angles.valueOf(card.startingBackResources[j]);
                }
                Front starterFront = new ResourceFront(startingResourcesTemp, points, covered);
                Card starterCard = new StarterCard(starterFront, flipped, startingBackResourcesTemp, card.cardID);
                starterDeck.add(starterCard);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
