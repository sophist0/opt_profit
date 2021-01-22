import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.math.*;
import java.io.FileWriter;
import java.io.IOException;


public class Node {
	
	private static int[] all_labels;	// These are not unique to the nodes but are unique to the goods they represent
	private static double total_profit = 0;	
	private static BigDecimal alpha;
	private static int sigfig = 0;

	private Node parent;
	private ArrayList<Integer> unexplored;
	private int label;
	private ArrayList<Integer> branch_nodes;
	private static OptParam param;

	/** 
	 *	Node class constructor for root used by OptProfit class
	 * 	@param ilabel initial node label
	 * 	@param iparam initial node class parameters
	 */
	public Node(int ilabel,OptParam iparam)
	{
		label = ilabel;
		param = iparam;
		try {
			sigfig = iparam.get_sigfig();
			alpha = new BigDecimal(iparam.get_alpha());
			alpha = alpha.setScale(sigfig,RoundingMode.HALF_UP);
			int n = param.get_num_prices() + 1;
			parent = null;
			all_labels = new int[n];
			for (int i=0; i<n; i++){
				all_labels[i] = i;
			}
		}
		catch (Exception e){
			System.out.println("Parameters Null.");
		}
		branch_nodes = new ArrayList<Integer>();
		branch_nodes.add(label);
		unexplored = this.init_unexplored();	
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
		unexplored = this.init_unexplored();	
	}

	/**
	 * Returns the profit from root to a particular leaf 
	 * @return branch profit
	 */
	public double branchTrunc_profit()
	{
		HashMap<String,Float> fmat = param.get_fmat();
		double[] prices = param.get_prices();
		double pval = 0;
		double frac = 1;
		String key = "0";
		for (int i=1; i<branch_nodes.size(); i++) {
			int label = branch_nodes.get(i);			
			String slabel = Integer.toString(label);
			key = key+slabel;
			double val = (double) fmat.get(key);		
			frac = frac * val * (1-prices[label-1]);
		}
		pval = frac * prices[branch_nodes.size()-2];
		return pval;
	}
	
	/**
	 * Returns the probability of arriving at a node in the permutation tree.
	 * This assumes no price sensitivity.
	 * @return branch profit
	 */
	public double branch_prob()
	{
		HashMap<String,Float> fmat = param.get_fmat();
		double frac = 1;
		String key = "0";
		
		for (int i=1; i<branch_nodes.size(); i++) {
			int label = branch_nodes.get(i);			
			String slabel = Integer.toString(label);
			key = key+slabel;
			double val = (double) fmat.get(key);
			frac = frac * val;
			}
		return frac;
	}


	/**
	 * Copy branch labels
	 * @param pbranch
	 * @return branch labels
	 */
	private ArrayList<Integer> copy_branch(ArrayList<Integer> pbranch)
	{
		ArrayList<Integer> branch = new ArrayList<Integer>();
		branch.addAll(pbranch);
		return branch;
	}
	
	/**
	 * The recursive function that expands truncated permutation tree
	 * and computes the profit at each none root node
	 * @return parent node
	 */
	public void expandTrunc()
	{
		if (label > 0) {
			double bp = branch_prob();
			BigDecimal bprob = new BigDecimal(bp);
			bprob = bprob.setScale(sigfig,RoundingMode.HALF_UP);	
			// if bprob < alpha delete subtree 
			if (bprob.compareTo(alpha) < 0) {
				while (unexplored.size() > 0) {
					unexplored.remove(0);
				}
			}
			else {
					double bprofit = branchTrunc_profit();
					total_profit += bprofit;
			}
		}
		while (unexplored.size() > 0) {
			Node nnode = next_node();	
			nnode.expandTrunc();
		}
	}
	
	/**
	 *	Set nodes unexplored labels
	 *  @return labels of unexplored nodes
	 */
	private ArrayList<Integer> init_unexplored()
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
	 * Returns threshold for truncating the subtree
	 * @return alpha BigDecimal
	 */
	public BigDecimal get_alpha()
	{
		return alpha;
	}
	
	/**
	 * Returns current nodes branch nodes
	 * @return unexplored nodes
	 */
	public ArrayList <Integer> get_branch_nodes()
	{
		ArrayList<Integer> cdata = new ArrayList<Integer>();
		cdata.addAll(branch_nodes);
		return cdata;
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
	 * Returns node classes optimization paramaters
	 * @return param
	 */
	public OptParam get_param()
	{
		return param;
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
	 * Returns significant figures 
	 * @return sigfig int
	 */
	public int get_sigfig()
	{
		return sigfig;
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
	 * Returns current nodes unexplored nodes
	 * @return unexplored nodes
	 */
	public ArrayList <Integer> get_unexplored()
	{
		ArrayList<Integer> cdata = new ArrayList<Integer>();
		cdata.addAll(unexplored);
		return cdata;
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
	
	/**
	 * Sets the values of the unexplored list
	 * @param n_unexplored
	 */
	public void set_unexplored(ArrayList<Integer> n_unexplored)
	{
		unexplored = n_unexplored;
	}
}
