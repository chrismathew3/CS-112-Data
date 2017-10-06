\package structures;

import java.util.ArrayList;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {
	
	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;
	
	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {
		
		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}
		
		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;
		
		// sort intervals on left and right end points
		sortIntervals(intervalsLeft, 'l');
		sortIntervals(intervalsRight,'r');
		
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = 
							getSortedEndPoints(intervalsLeft, intervalsRight);
		
		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
		
		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
	}
	
	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		// COMPLETE THIS METHOD
		//Sort the ArrayList
		sort(intervals, lr);
		printIntervals(intervals);
		
	}
	/**
     * Sorts an ArrayList in ascending order using insertion sort
     */
    private static void sort( ArrayList<Interval> arr, char lr )
    {
        int valueOfLeft;
        int valueOfRight;
        Interval valueOfInterval;
        int holePos;
        //Check if lr is l or r and sort respectively
        if(lr == 'l'){
        	//create a new array list for leftSortedEndpoints
	        // At the beginning of each iteration arr[0..k-1] has been sorted, and the loop then
	        // inserts element k into its proper place
	        for( int k=1; k<arr.size(); k++ )
	        {
	            // make a copy of the value to insert
	            valueOfLeft = arr.get(k).leftEndPoint;
	            valueOfInterval = arr.get(k);
	            
	            // The kth position is where the "hole" starts
	            holePos = k;
	            
	            // Move the "hole" left until we either get the the end of the array
	            // or the element left of the hole is <= to the value we're inserting
	            while( holePos > 0 && arr.get(holePos-1).leftEndPoint > valueOfLeft ){
	                // move the hole to the left
	                arr.set( holePos, arr.get(holePos-1) );
	                holePos--;
	            }
	            
	            // copy the value into the hole
	            arr.set( holePos, valueOfInterval );
	        }
        }else{
        	for( int k=1; k<arr.size(); k++ ){
                valueOfRight = arr.get(k).rightEndPoint;
                valueOfInterval = arr.get(k);
                holePos = k;
                while( holePos > 0 && arr.get(holePos-1).rightEndPoint > valueOfRight ){
                    arr.set( holePos, arr.get(holePos-1) );
                    holePos--;
                }
                arr.set( holePos, valueOfInterval);
            }
        }
    }
    //Print out ArrayList for debugging purposes
    private static void printIntervals( ArrayList<Interval> arr )
    {
        for( int i=0; i<arr.size(); i++ )
            System.out.println( arr.get(i) );
        System.out.println();
    }
    private static void printIntegers( ArrayList<Integer> arr )
    {
        for( int i=0; i<arr.size(); i++ )
            System.out.println( arr.get(i) );
        System.out.println();
    }
	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		// COMPLETE THIS METHOD
		//Create a new ArrayList<Integer> called Points
		//For each element of left list, search to see if contained in points, if false then add to points
		//Then do it for the right list
		//then run sort on the list to double check
		ArrayList<Integer> points = new ArrayList<Integer>();
		for(int i = 0;i < leftSortedIntervals.size();i++){
			if(search(points, leftSortedIntervals.get(i).leftEndPoint) == false){
				points.add(leftSortedIntervals.get(i).leftEndPoint);
			}
		}
		for(int i = 0;i < rightSortedIntervals.size();i++){
			if(search(points, rightSortedIntervals.get(i).rightEndPoint) == false){
				points.add(rightSortedIntervals.get(i).rightEndPoint);
			}
		}
		sortInteger(points);
		printIntegers(points);
		return points;
	}
	private static void sortInteger(ArrayList<Integer> arr){
		int val;
        int holePos;
        for( int k=1; k<arr.size(); k++ ){
            val = arr.get(k);
            holePos = k;
            while( holePos > 0 && arr.get(holePos-1) > val ){
                arr.set( holePos, arr.get(holePos-1) );
                holePos--;
            }
            arr.set( holePos, val );
        }
	}
	private static boolean search(ArrayList<Integer> arr, int target){
		for(int i = 0; i < arr.size();i++){
				if(arr.get(i) == target){
					return true;
				}
		}
		return false;
	}
	
	/**
	 * Builds the interval tree structure given a sorted array list of end points
	 * without duplicates.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		float holder;
		int size;
		int temps;
		float v1;
		float v2;
		float x;
		Queue<IntervalTreeNode> Q = new Queue<IntervalTreeNode>();
		for(int i = 0; i < endPoints.size();i++){
			holder = (float)endPoints.get(i);
			IntervalTreeNode t = new IntervalTreeNode( holder, holder, holder);
			t.leftIntervals = new ArrayList<Interval>();
			t.rightIntervals = new ArrayList<Interval>();
			Q.enqueue(t);
		}
		while(Q.size() != 0){
			size = Q.size();
			if(size == 1){
				IntervalTreeNode T = Q.dequeue();
				return T;
			}
			
			temps = size;
			while(temps > 1){
				IntervalTreeNode T1 = Q.dequeue();
				IntervalTreeNode T2 = Q.dequeue();
				v1 = T1.maxSplitValue;
				v2 = T2.minSplitValue;
				x = (v1 + v2)/(2);
				IntervalTreeNode N = new IntervalTreeNode(x, T1.minSplitValue, T2.maxSplitValue);
				N.leftIntervals = new ArrayList<Interval>();
				N.rightIntervals = new ArrayList<Interval>();
				N.leftChild = T1;
				N.rightChild = T2;
				Q.enqueue(N);
				temps = temps - 2;
			}
			if(temps == 1){
				Q.enqueue(Q.dequeue());
			}
			size = Q.size();
		}
		
		return Q.dequeue();
	}
	
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		for (Interval x : leftSortedIntervals){
			mapLeft(x, root);
		
		}
		for (Interval x : rightSortedIntervals){
			mapRight(x, root);
			
		}
		return;

	}
	private void mapLeft(Interval x, IntervalTreeNode r){
		if (x.contains(r.splitValue)){
			r.leftIntervals.add(x);
			return;
		}
		if (r.splitValue < x.leftEndPoint){
			mapLeft(x, r.rightChild);
		}
		else {
			mapLeft(x, r.leftChild);
		}
	}
	private void mapRight(Interval x, IntervalTreeNode r){
		if (x.contains(r.splitValue)){
			r.rightIntervals.add(x);
			return;
		}
		if (r.splitValue < x.leftEndPoint){
			mapRight(x, r.rightChild);
		}
		else {
			mapRight(x, r.leftChild);
		}
	}
	
	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		ArrayList<Interval> resultList = new ArrayList<Interval>();
		return query(root, q, resultList);
	}
	private ArrayList<Interval> query(IntervalTreeNode R,Interval s, ArrayList<Interval> intersectList){
		ArrayList<Interval> Llist = R.leftIntervals;
		ArrayList<Interval> Rlist = R.rightIntervals;
		IntervalTreeNode Lsub = R.leftChild;
		IntervalTreeNode Rsub = R.rightChild;
		float splitval = R.splitValue;
		if(isLeaf(R)){
			return intersectList;
		}else if(s.contains(splitval)){
			if(Llist != null){
				for (Interval x : Llist){
					intersectList.add(x);
				}
			}
			query(Rsub,s, intersectList);
			query(Lsub,s, intersectList);
		}else if (splitval < s.leftEndPoint){
			if (Rlist != null){
				int i = Rlist.size();
				while(i > 0 && intersects(Rlist.get(i-1), s)){
					intersectList.add(Rlist.get(i-1));
					i--;
				}
			}
			query(Rsub, s, intersectList);
		}		
		else if (splitval > s.rightEndPoint){
			if (Llist != null){
				int i = 0;
				while ((i < Llist.size()) && intersects(Llist.get(i), s)){
					intersectList.add(Llist.get(i));
					i++;
				}
			}
			query(Lsub, s, intersectList);
		}
		return intersectList;
	}

	private boolean isLeaf(IntervalTreeNode l){
		if(l.leftChild == null && l.rightChild == null){
			return true;
		}else{
			return false;
		}
	}
	private static boolean intersects(Interval a, Interval b){
		if (a.leftEndPoint >= b.leftEndPoint){
			if (a.leftEndPoint == b.leftEndPoint){
				return true;
			}
			else if (a.leftEndPoint <= b.rightEndPoint){
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if (a.rightEndPoint >= b.leftEndPoint){
				return true;
			}
			else{
				return false;
			}
		}
	}
}