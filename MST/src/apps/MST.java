package apps;

import structures.*;
import java.util.ArrayList;
import java.util.Iterator;
public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		//1. Create an empty Partial Tree List
		PartialTreeList L = new PartialTreeList();
		//2.Separately for each vertex v in the graph:
		for(int i = 0; i < graph.vertices.length; i++){
			//Create a partial tree T containing only v.
			PartialTree T = new PartialTree(graph.vertices[i]);
			//Mark v as belonging to T (this will be implemented in a particular way in the code).
			graph.vertices[i].parent = T.getRoot();
			System.out.println(graph.vertices[i].parent);
			//Create a priority queue (heap) P and associate it with T.	
			MinHeap p = T.getArcs();
			//Insert all of the arcs (edges) connected to v into P. The lower the weight on an arc, the higher its priority.
			for(Vertex.Neighbor nbr = graph.vertices[i].neighbors; nbr != null; nbr=nbr.next){
				Vertex V1 = graph.vertices[i];
				Vertex V2 = nbr.vertex;
				PartialTree.Arc a = new PartialTree.Arc(V1, V2, nbr.weight);
				p.insert(a);
			}
			//Add the partial tree T to the list L.
			L.append(T);
		}
		printList(L);
		return L;
	}
	private static void printList(PartialTreeList a){
		Iterator<PartialTree> iter = a.iterator();
		   while (iter.hasNext()) {
		       PartialTree pt = iter.next();
		       System.out.println(pt.toString());
		   }
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		//3. Remove the first partial tree PTX from L. Let PQX be PTX's priority queue.
		//PartialTree PTX = ptlist.remove();
		//MinHeap PQX = PTX.getArcs();
		//4. Remove the highest-priority arc from PQX. Say this arc is Î±. Let v1 and v2 be the two vertices connected by Î±, where v1 belongs to PTX.
		//PartialTree.Arc a;
		//a = PQX.deleteMin();
		//5.If v2 also belongs to PTX, go back to Step 4 and pick the next highest priority arc, otherwise continue to the next step.
		//6. Report Î± - this is a component of the minimum spanning tree.
		//7.Find the partial tree PTY to which v2 belongs. Remove PTY from the partial tree list L. Let PQY be PTY's priority queue.
		//8.Combine PTX and PTY. This includes merging the priority queues PQX and PQY into a single priority queue. Append the resulting tree to the end of L.
		//9. If there is more than one tree in L, go to Step 3.
		ArrayList<PartialTree.Arc> arc = new ArrayList<>();
		while (ptlist.size() > 1) {
			PartialTree T = ptlist.remove();
			 MinHeap<PartialTree.Arc> P = T.getArcs();
			 PartialTree.Arc min = P.deleteMin();
			 
			 while (min != null) {   
				 Vertex v1 = min.v1;
	             Vertex v2 = min.v2;
	             PartialTree TT;
	             TT = ptlist.removeTreeContaining(v1);
	             
	             if (TT == null) {
	            	 TT = ptlist.removeTreeContaining(v2);
	             }
	             if (TT != null) {  
	            	 T.merge(TT);
	                 arc.add(min);
	                 ptlist.append(T); 
	                 break;
	             }
	             min = P.deleteMin();
			 }
		 }
		 return arc;

	}
}