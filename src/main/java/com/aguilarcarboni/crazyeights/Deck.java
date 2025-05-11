package com.aguilarcarboni.crazyeights;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;
    private static final String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private static final char[] SUITS = {'H', 'D', 'C', 'S'}; // Hearts, Diamonds, Clubs, Spades

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        for (char suit : SUITS) {
            for (String rank : RANKS) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public List<Card> getAllCards() {
        return new ArrayList<>(cards);
    }

    public List<String> getCardStrings() {
        List<String> cardStrings = new ArrayList<>();
        for (Card card : cards) {
            cardStrings.add(card.toString());
        }
        return cardStrings;
    }
} 