package intelligence;

import java.util.Comparator;

/**
 * A comparator is used to order the priority queue that is implemented in the A* Search
 * @author Wesley
 *
 */
public class NodeComparator implements Comparator<Node>{

	@Override
	public int compare(Node A, Node B) {
		if(A.getF() < B.getF()){
			return -1;
		} else if(A.getF() > B.getF()){
			return 1;
		} else {
			return 0;
		}
	}

}
