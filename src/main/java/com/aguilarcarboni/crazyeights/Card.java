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

    public int getPoints() {
        // Non-numeric cards (A, J, Q, K, 8) count as 10 points
        if (rank.equals("A") || rank.equals("J") || rank.equals("Q") || rank.equals("K") || rank.equals("8")) {
            return 10;
        }
        // Numeric cards (2-7, 9, 10) count as their face value
        return Integer.parseInt(rank);
    }

    public static Card fromString(String cardStr) {
        if (cardStr == null || cardStr.length() < 2) {
            throw new IllegalArgumentException("Invalid card string format");
        }
        String rank = cardStr.substring(0, cardStr.length() - 1);
        char suit = cardStr.charAt(cardStr.length() - 1);
        return new Card(rank, suit);
    }
} 