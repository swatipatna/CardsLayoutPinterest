package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CardLayout {
  public static void main(String[] args) {
    List<Integer> list1 = new ArrayList<>(Arrays.asList(27, 21, 17, 15, 12, 9, 8, 7, 2));
    stackCards(list1, 3);

    List<Integer> list2 = new ArrayList<>(Arrays.asList(27, 17, 15, 12, 8, 7, 2));
    stackCards(list2, 3);
  }

  /******* Utility methods *******/

  private static void stackCards(List<Integer> list, int numColumns) {
    System.out.println("Stacking cards into " + numColumns + " columns from " + list);
    CardLayout pinterest = new CardLayout();
    List<List<Integer>> columns = pinterest.arrangeCards(list, numColumns);
    for (int i = 0; i < columns.size(); i++) {
      System.out.println("column" + (i + 1) + ": " + printList(columns.get(i)));
    }
  }

  private static String printList(List<Integer> list) {
    StringBuilder sb = new StringBuilder();
    for (Integer value : list) {
      sb.append(value + ", ");
    }
    return sb.toString();
  }

  private static Integer sum(List<Integer> list) {
    Integer sum = 0;
    for (Integer i : list) {
      sum += i;
    }
    return sum;
  }

  /**
   * Creates lists for given number of columns and stack cards from the original sorted list into the columns
   * A Column is a list of cards (i.e. length of cards)
   * @param cards Original List of cards, contains length of cards
   * @param numColumns number of columns in the layout
   * @return List of columns
   */
  public List<List<Integer>> arrangeCards(List<Integer> cards, int numColumns) {
    // Create a copy of original list of cards
    List<Integer> sortedCards = new ArrayList<>(cards);
    // Sort the copy in descending order
    Collections.sort(cards, Collections.reverseOrder());

    Integer optimum = (int) Math.ceil((double) sum(sortedCards) / numColumns);

    List<List<Integer>> columns = new ArrayList<>();

    for (int i = 0; i < numColumns; i++) {
      // Initialising the list for ith Column
      List<Integer> column = new ArrayList<>();

      // Filling the ith Column

      // If the length of longest card is more than optimum then just put it
      // inside this column and move to next column
      if (sortedCards.get(0) >= optimum) {
        column.add(sortedCards.remove(0));
      } else {
        // Pick cards using binary search on sorted list of cards
        // and stuff them in column
        int currLength = 0;
        int suitableIndex = binarySearchForNextSuitableCard(optimum - currLength, sortedCards, 0,
            sortedCards.size() - 1);
        while (suitableIndex < sortedCards.size()) {
          // Removed this card from original list and add it to the column
          Integer cardToAdd = sortedCards.remove(suitableIndex);
          column.add(cardToAdd);
          currLength += cardToAdd;
          suitableIndex = binarySearchForNextSuitableCard(optimum - currLength, sortedCards, 0, sortedCards.size() - 1);
        }
      }
      columns.add(column);
    }

    // If there are still some cards remaining, add them one by one to the shortest available column
    while (!sortedCards.isEmpty()) {
      int shortestIndex = findShortestColumn(columns);
      columns.get(shortestIndex).add(sortedCards.remove(0));
    }

    return columns;

  }

  /**
   * Given a List of Columns finds the shortest column
   * @param columns
   * @return the index of the shortest column
   */
  private int findShortestColumn(List<List<Integer>> columns) {
    int shortestLength = Integer.MAX_VALUE;
    int shortestIndex = -1;
    for (int i = 0; i < columns.size(); i++) {
      if (sum(columns.get(i)) < shortestLength) {
        shortestIndex = i;
      }
    }
    return shortestIndex;
  }

  /**
   * Recursively performs binary search for the suitable card in the list of sorted cards
   * Suitable card is the highest card below or equal to max length
   * @param maxLength Highest allowed length for suitable card
   * @param sortedCards list of sorted cards
   * @param left left index which defines the subList to work with
   * @param right right index which defines the subList to work with
   * @return index of the suitable card in the original list of sorted cards
   */
  private int binarySearchForNextSuitableCard(Integer maxLength, List<Integer> sortedCards, int left, int right) {
    if (left > right) {
      return left;
    }
    int mid = (left + right) / 2;
    if (sortedCards.get(mid) == maxLength) {
      return mid;
    } else if (sortedCards.get(mid) < maxLength) {
      return binarySearchForNextSuitableCard(maxLength, sortedCards, left, mid - 1);
    } else {
      return binarySearchForNextSuitableCard(maxLength, sortedCards, mid + 1, right);
    }
  }
}
