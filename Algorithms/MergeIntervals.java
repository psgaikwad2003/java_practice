import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Merge Intervals & Interval Scheduling Algorithm Suite
 *
 * Implements core interval manipulation algorithms:
 * 1. Merge Overlapping Intervals - O(n log n)
 * 2. Insert Interval into Sorted Non-overlapping Set - O(n)
 * 3. Meeting Rooms II (Min Rooms / Platforms Required) - O(n log n)
 * 4. Interval List Intersections - O(n + m)
 * 5. Minimum Interval Removals for Non-overlapping Set (Activity Selection) - O(n log n)
 */
public class MergeIntervals {

    // -------------------------------------------------------------
    // 1. Merge Overlapping Intervals
    // Time Complexity: O(n log n), Space: O(n)
    // -------------------------------------------------------------
    public static int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) return intervals;

        // Sort by start time ascending
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        List<int[]> merged = new ArrayList<>();
        int[] current = intervals[0];
        merged.add(current);

        for (int i = 1; i < intervals.length; i++) {
            int[] next = intervals[i];
            if (next[0] <= current[1]) {
                // Overlap exists: extend the end of the current interval
                current[1] = Math.max(current[1], next[1]);
            } else {
                // No overlap: advance to next interval
                current = next;
                merged.add(current);
            }
        }

        return merged.toArray(new int[merged.size()][]);
    }

    // -------------------------------------------------------------
    // 2. Insert Interval into Sorted Non-overlapping Intervals
    // Time Complexity: O(n), Space: O(n)
    // -------------------------------------------------------------
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        if (newInterval == null) return intervals;
        if (intervals == null || intervals.length == 0) return new int[][]{newInterval};

        List<int[]> result = new ArrayList<>();
        int i = 0;
        int n = intervals.length;

        // Step 1: Add all intervals ending before newInterval starts
        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i]);
            i++;
        }

        // Step 2: Merge overlapping intervals with newInterval
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval);

        // Step 3: Add remaining intervals
        while (i < n) {
            result.add(intervals[i]);
            i++;
        }

        return result.toArray(new int[result.size()][]);
    }

    // -------------------------------------------------------------
    // 3. Meeting Rooms II: Minimum Rooms / Platforms Required
    // Time Complexity: O(n log n), Space: O(n)
    // -------------------------------------------------------------
    public static int minMeetingRooms(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;

        int n = intervals.length;
        int[] startTimes = new int[n];
        int[] endTimes = new int[n];

        for (int i = 0; i < n; i++) {
            startTimes[i] = intervals[i][0];
            endTimes[i] = intervals[i][1];
        }

        Arrays.sort(startTimes);
        Arrays.sort(endTimes);

        int roomsNeeded = 0;
        int endPointer = 0;

        for (int startPointer = 0; startPointer < n; startPointer++) {
            if (startTimes[startPointer] < endTimes[endPointer]) {
                // A meeting starts before the earliest ending meeting finishes -> need a new room
                roomsNeeded++;
            } else {
                // An existing room is freed up
                endPointer++;
            }
        }

        return roomsNeeded;
    }

    // Min-Heap approach for Meeting Rooms II
    public static int minMeetingRoomsWithHeap(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;

        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        PriorityQueue<Integer> endMinHeap = new PriorityQueue<>();

        for (int[] interval : intervals) {
            if (!endMinHeap.isEmpty() && endMinHeap.peek() <= interval[0]) {
                endMinHeap.poll(); // Reuse existing room
            }
            endMinHeap.offer(interval[1]);
        }

        return endMinHeap.size();
    }

    // -------------------------------------------------------------
    // 4. Interval List Intersections
    // Time Complexity: O(n + m), Space: O(n + m)
    // -------------------------------------------------------------
    public static int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        if (firstList == null || secondList == null) return new int[0][0];

        List<int[]> intersections = new ArrayList<>();
        int i = 0, j = 0;

        while (i < firstList.length && j < secondList.length) {
            int low = Math.max(firstList[i][0], secondList[j][0]);
            int high = Math.min(firstList[i][1], secondList[j][1]);

            if (low <= high) {
                intersections.add(new int[]{low, high});
            }

            // Move pointer of interval that finishes earlier
            if (firstList[i][1] < secondList[j][1]) {
                i++;
            } else {
                j++;
            }
        }

        return intersections.toArray(new int[intersections.size()][]);
    }

    // -------------------------------------------------------------
    // 5. Erase Overlapping Intervals (Minimum Removals for Non-overlap)
    // Greedy Activity Selection by earliest finish time: O(n log n)
    // -------------------------------------------------------------
    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) return 0;

        // Sort by end time ascending
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));

        int countKept = 1;
        int lastEnd = intervals[0][1];

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= lastEnd) {
                countKept++;
                lastEnd = intervals[i][1];
            }
        }

        return intervals.length - countKept;
    }

    public static String formatIntervals(int[][] intervals) {
        if (intervals == null) return "null";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < intervals.length; i++) {
            sb.append(Arrays.toString(intervals[i]));
            if (i < intervals.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("===================================================================");
        System.out.println("               MERGE INTERVALS ALGORITHM SUITE                     ");
        System.out.println("===================================================================");

        // 1. Merge Overlapping Intervals Demo
        System.out.println("\n1. Merge Overlapping Intervals:");
        int[][] intervals1 = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        System.out.println("  Input    : " + formatIntervals(intervals1));
        System.out.println("  Merged   : " + formatIntervals(merge(intervals1)));

        int[][] intervals2 = {{1, 4}, {4, 5}};
        System.out.println("  Input    : " + formatIntervals(intervals2));
        System.out.println("  Merged   : " + formatIntervals(merge(intervals2)));

        // 2. Insert Interval Demo
        System.out.println("\n2. Insert Interval:");
        int[][] sortedSet = {{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}};
        int[] newInterval = {4, 8};
        System.out.println("  Existing : " + formatIntervals(sortedSet));
        System.out.println("  Insert   : " + Arrays.toString(newInterval));
        System.out.println("  Result   : " + formatIntervals(insert(sortedSet, newInterval)));

        // 3. Meeting Rooms II Demo
        System.out.println("\n3. Meeting Rooms II (Min Rooms Required):");
        int[][] meetings = {{0, 30}, {5, 10}, {15, 20}};
        System.out.println("  Meetings : " + formatIntervals(meetings));
        int rooms1 = minMeetingRooms(meetings);
        int roomsHeap = minMeetingRoomsWithHeap(meetings);
        System.out.println("  Rooms (Two-Pointer Sweep) : " + rooms1);
        System.out.println("  Rooms (Min-Heap)          : " + roomsHeap);

        int[][] meetings2 = {{7, 10}, {2, 4}};
        System.out.println("  Meetings : " + formatIntervals(meetings2));
        System.out.println("  Rooms Needed              : " + minMeetingRooms(meetings2));

        // 4. Interval List Intersections Demo
        System.out.println("\n4. Interval List Intersections:");
        int[][] firstList = {{0, 2}, {5, 10}, {13, 23}, {24, 25}};
        int[][] secondList = {{1, 5}, {8, 12}, {15, 24}, {25, 26}};
        System.out.println("  List A   : " + formatIntervals(firstList));
        System.out.println("  List B   : " + formatIntervals(secondList));
        System.out.println("  Overlap  : " + formatIntervals(intervalIntersection(firstList, secondList)));

        // 5. Erase Overlapping Intervals Demo
        System.out.println("\n5. Non-overlapping Intervals (Min Removals):");
        int[][] overlapSet = {{1, 2}, {2, 3}, {3, 4}, {1, 3}};
        System.out.println("  Intervals : " + formatIntervals(overlapSet));
        System.out.println("  Min removals to make non-overlapping: " + eraseOverlapIntervals(overlapSet) + " (Expected: 1)");

        System.out.println("\n===================================================================");
    }
}
