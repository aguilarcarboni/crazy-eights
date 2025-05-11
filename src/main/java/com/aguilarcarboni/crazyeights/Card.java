package com.aguilarcarboni.crazyeights;

public class Card {
    private final String rank;
    private final char suit;

    public Card(String rank, char suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return rank + suit;
    }

    public String getRank() {
        return rank;
    }

    public char getSuit() {
        return suit;
    }
} 