import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Node {
	
	private static int[] all_labels;	// These are not unique to the nodes but are unique to the goods they represent
	private static double total_profit = 0;	
	private static OptParam param;

	private int label;
	private Node parent;
	private ArrayList<Integer> unexplored;
	private ArrayList<Integer> branch_nodes;
	
	/** 
	 *	Node class constructor for root used by OptProfit class
	 * 	@param ilabel initial node label
	 * 	@param iparent initial node parent
	 */
	public Node(int ilabel,OptParam iparam)
	{
		param = iparam;		
		int n = param.get_num_prices() + 1;
		label = ilabel;
		parent = null;
		all_labels = new int[n];
		for (int i=0; i<n; i++)
		{
			all_labels[i] = i;
		}
		branch_nodes = new ArrayList<Integer>();
		branch_nodes.add(label);
		unexplored = this.set_unexplored();	
	}
	
	/** 
	 *	Node class constructor
	 * 	@param ilabel initial node label
	 * 	@param iparent initial node parent
	 */
	public Node(int ilabel, Node iparent)
	{
		label = ilabel;
		parent = iparent;
		branch_nodes = copy_branch(parent.branch_nodes);
		branch_nodes.add(label);
		unexplored = this.set_unexplored();	
	}

	/**
	 * Returns the profit from root to a particular leaf 
	 * @return branch profit
	 */
	public double branch_profit()
	{
		HashMap<String,Float> fmat = param.get_fmat();
		double[] prices = param.get_prices();
		double pval = 0;
		double frac = 1;
		for (int i=1; i<branch_nodes.size(); i++) {
			int n1 = branch_nodes.get(i-1);
			int n2 = branch_nodes.get(i);
			
			String s1 = Integer.toString(n1);
			String s2 = Integer.toString(n2);
			String key = s1+s2;
		
			double cfrac = fmat.get(key);
			frac = frac * cfrac * (1-prices[n2-1]);
			pval += frac * prices[n2-1];
		}
		return pval;
	}

	/**
	 * Copy branch labels
	 * @param pbranch
	 * @return branch labels
	 */
	private ArrayList<Integer> copy_branch(ArrayList<Integer> pbranch)
	{
		ArrayList<Integer> branch = new ArrayList<Integer>();
		for (int i=0; i<pbranch.size(); i++) {
			branch.add(pbranch.get(i));
		}
		return branch;
	}
	
	/**
	 * The recursive function that expands the permutation tree
	 * @return parent node
	 */
	public Node expand()
	{
		/* if leaf print branch and compute profit */	
		if (unexplored.size() <= 0){
			// print_vec(branch_nodes);
			double bprofit = branch_profit();
			total_profit += bprofit;
		}
		
		while (unexplored.size() > 0) {
			Node nnode = next_node();	
			nnode.expand();
		}
		return this.parent;
	}
	
	/**
	 * Returns nodes label
	 * @return label
	 */
	public int get_label()
	{
		return label;
	}
	
	/**
	 * Returns nodes parent
	 * @return parent node
	 */
	public Node get_parent()
	{
		return parent;
	}
	
	/**
	 *  Get total profit
	 *  @return total profit
	 */
	public double get_total_profit() 
	{
		return total_profit;
	}
	
	/**
	 * Turn the first unexplored node at the current node into a child node. 
	 * Assumes calling function updates current nodes list of unexplored nodes.
	 * @return next unexplored node
	 */
	private Node next_node() 
	{
		int next_label = unexplored.get(0);
		Node next_node = new Node(next_label, this);
		unexplored.remove(0);
		return next_node;
	}
	
	/**
	 * Reset total profit to zero
	 * @return total profit
	 */
	public void reset_total_profit()
	{
		total_profit = 0;
	}
	
	/**
	 *	Set nodes unexplored labels
	 *  @return labels of unexplored nodes
	 */
	private ArrayList<Integer> set_unexplored()
	{
		ArrayList<Integer> uvec = new ArrayList<Integer>();
		for (int i=0; i < all_labels.length; i++) {
			int clabel = all_labels[i];
			if (branch_nodes.contains(clabel) == false) {
				uvec.add(clabel);
			}
		}
		return uvec;
	}	
	   
    /**
     *	Print out node label
     */
	public void print_label() 
	{
		System.out.println(label);
	}
	
	 /**
     *	Print out node parent
     */
	public void print_parent() 
	{
		System.out.println(parent);
	}
	
	/**
     *	Print out total profit computed over all nodes in class
     */
	public void print_total_profit() 
	{
		System.out.println(total_profit);
	}
		
	/**
	 * Print vec contents
	 * @param vec
	 */
	public void print_vec(ArrayList<Integer> vec)
	{
		System.out.println();
		for (int i = 0; i < vec.size(); i++) {
			System.out.print(vec.get(i)+", ");
		}
		System.out.println();
		System.out.println();
	}
}
