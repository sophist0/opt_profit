import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * Use OptParam to pass out prices and profit together and pass them all into the Node class
 */

public class OptProfit {
	
	private OptParam param;
	private Node root;
	
	/**
	 * Class constructor
	 * @param iparam
	 */
	public OptProfit(OptParam iparam)
	{
		param = iparam;
	}
	
	/**
	 * Loads parameters and starts the price` optimization
	 * @param args
	 */
	public static void main(String[] args)
	{
		OptParam lparam = new OptParam();
		try {
			lparam = lparam.loadParams("param.txt");
		} catch (Exception e) {
			System.out.println("Failed to read parameters.");
			e.printStackTrace();
		}
		OptProfit opt = new OptProfit(lparam);
		double[] oprices = opt.opt_prices();
		System.out.println();
		System.out.println("Optimized prices: "+Arrays.toString(oprices));
		OptParam oparam = opt.get_param();
		System.out.println("Profit: " + oparam.get_profit());
		System.out.println();	
	}

	/**
	 * Estimate the gradient of the profit
	 * @param i
	 * @param param
	 * @return gradient
	 */
	public double get_gradient(int i, OptParam param)
	{
		double delta = 0.01;
		double[] prices = param.get_prices();
		double last_profit = param.get_profit();
		if (last_profit < 0) {
			last_profit = get_profit(param); 
			param.set_profit(last_profit);
		}
		
		prices[i] += delta;
		HashMap<String, Float> fmat = param.get_fmat();
		OptParam cparam = new OptParam(prices,fmat);
		double test_profit = get_profit(cparam);
		double grad = (test_profit - last_profit) / delta;

		return grad;
	}

	/**
	 * Compute the profit over all permutations
	 * @param cparam
	 * @return profit
	 */
	public double get_profit(OptParam cparam)
	{	
		Node root = new Node(0, cparam);
		root.reset_total_profit();
		root.expand();
		return root.get_total_profit();
	}

	/**
	 * Update prices using Stochastic Gradient Ascent
	 * @return updated prices
	 */
	public double[] opt_prices()
	{
		int it = 1000;
		int nprices = param.get_num_prices();
		double neu = 0.01;
		for (int i=0; i<it; i++){
			int idx = (int) (Math.random() * nprices); // random num
			double grad = get_gradient(idx,param);
			double[] tprices = param.get_prices();	
			tprices[idx] += (neu * grad);
			
			if (tprices[idx] < 0) {
				tprices[idx] = 0;
			}
			else if (tprices[idx] > 1) {
				tprices[idx] = 1;
			}
			param.set_prices(tprices);
			double tprofit = get_profit(param);
			param.set_profit(tprofit);
		}
		return param.get_prices();
	}

	/**
	 * Returns current parameters
	 * @return param
	 */
	public OptParam get_param()
	{
		return param;
	}
	
	/**
	 * Set new parameter values
	 * @param nparam
	 */
	public void set_param(OptParam nparam)
	{
		param = nparam;
	}
}
