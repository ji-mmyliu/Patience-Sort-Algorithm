/**
 * Names: Jimmy Liu, Josh Liu, Di Hu
 * Date: March 4, 2021
 * Teacher: Ms. Krasteva
 * Description: This class contains the methods require to sort an integer array using the Patience Sort algorithm
 */

import java.util.ArrayList;

/**
 * This class is used to sort an array using the Patience Sort algorithm
 * 
 * @param E The Object type that the array to sort is going to be in
 * @author Jimmy Liu
 */
public class PatienceSort<E> {
    /**
     * This 2D arraylist contains arraylists of arraylists which store the patience
     * sort buckets
     */
    private ArrayList<ArrayList<E>> buckets;

    /** This arraylist contains the top element of each bucket */
    private ArrayList<E> top;

    /**
     * This method is used to compare two parametized objects a and b
     * 
     * @param a One item to compare
     * @param b Another item to compare
     * @return True if item a is "less than" item b according to the implemeted
     *         Comparable<E> interface
     * @throws ClassCastException If the object type E does not implement the
     *                            Comparable<E> interface
     */
    private boolean lessThan(E a, E b) throws ClassCastException {
        if (!(a instanceof Comparable<?>)) {
            throw new ClassCastException(
                    "Type specified does not implement the Comparable<" + a.getClass().getName() + "> interface");
        }
        Comparable<E> c = (Comparable<E>) a;
        return c.compareTo(b) < 0;
    }

    /**
     * This method reverses an arraylist and returns the result
     * 
     * @param prev The original arraylist
     * @return The arraylist after it has been reversed
     */
    private ArrayList<E> reverse(ArrayList<E> prev) {
        ArrayList<E> toReturn = new ArrayList<E>();
        for (int i = prev.size() - 1; i >= 0; i--) {
            toReturn.add(prev.get(i));
        }
        return toReturn;
    }

    /**
     * This method returns a lower-bound binary search position. It gives the
     * leftmost position that the key is eligible to fit into
     * 
     * @param key The number to be placed
     * @return The leftmost eligible position for the key specified
     */
    private int binarySearch(E key) throws Exception {
        int lft = 0, rit = top.size() + 1;
        int ans = 0;
        while (lft < rit) {
            int mid = (lft + rit) / 2;
            if (mid < top.size() && lessThan(top.get(mid), key)) {
                lft = mid + 1;
            } else {
                rit = mid;
                ans = mid;
            }
        }
        return ans;
    }

    /**
     * This helper method merges two array lists together and sets the merged array
     * in the correct index for the next merge
     * 
     * @param buckets The arraylist of arraylist of buckets
     * @param idx     The left index at which to merge with
     */
    private void merge(ArrayList<ArrayList<E>> buckets, int idx) {
        ArrayList<E> tmp = new ArrayList<E>();
        int j = 0;

        // Merge both lists
        for (E val : buckets.get(idx)) {
            while (j < buckets.get(idx + 1).size() && lessThan(buckets.get(idx + 1).get(j), val)) {
                tmp.add(buckets.get(idx + 1).get(j++));
            }
            tmp.add(val);
        }

        // Merge any that are left in this list
        while (j < buckets.get(idx + 1).size()) {
            tmp.add(buckets.get(idx + 1).get(j++));
        }

        // Empty both buckets
        buckets.set(idx, new ArrayList<E>());
        buckets.set(idx + 1, new ArrayList<E>());

        // Set new bucket
        buckets.set(idx / 2, tmp);
    }

    /**
     * This method sorts an array using the Patience Sort algorithm
     * 
     * @param array The array to be sorted
     * @throws Exception
     */
    public void patienceSort(E array[]) throws Exception {
        top = new ArrayList<E>();
        buckets = new ArrayList<ArrayList<E>>();

        for (int i = 0; i < array.length; i++) {
            if (top.isEmpty()) {
                top.add(array[i]);
                buckets.add(new ArrayList<E>());
                buckets.get(0).add(array[i]);
            } else {
                int pos = binarySearch(array[i]);
                if (pos >= top.size()) {
                    top.add(array[i]);
                    buckets.add(new ArrayList<E>());
                    buckets.get(buckets.size() - 1).add(array[i]);
                } else {
                    top.set(pos, array[i]);
                    buckets.get(pos).add(array[i]);
                }
            }
        }

        // Reverse buckets for sorting
        for (int i = 0; i < buckets.size(); i++) {
            buckets.set(i, reverse(buckets.get(i)));
        }

        // Perform merge sort
        while (buckets.size() > 1) {
            // Every even bucket get's merged with it's odd counterpart
            for (int i = 0; i + 1 < buckets.size(); i += 2) {
                merge(buckets, i);
            }

            // Move the leftover bucket to the correct index
            if (buckets.size() % 2 == 1) {
                buckets.set(buckets.size() / 2, buckets.get(buckets.size() - 1));
                buckets.remove(buckets.size() - 1);
            }

            // Remove all empty arraylists from the end
            while (buckets.get(buckets.size() - 1).size() == 0) {
                buckets.remove(buckets.size() - 1);
            }
        }

        // Set values back into original array to sort it
        for (int i = 0; i < array.length; i++) {
            array[i] = buckets.get(0).get(i);
        }

        // Finished sorting!
    }
}