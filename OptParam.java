import java.util.Arrays;
import java.util.HashMap;
import java.io.*;

public class OptParam {
	
	private double profit;
	private double[] prices;
	private HashMap<String,Float> fmat;
	
	/**
	 * Empty constructor for the optimization parameters
	 */
	public OptParam()
	{
		profit = -1;
	}

	
	/**
	 * Constructor for the optimization parameters
	 * @param prices
	 * @param fmat
	 */
	public OptParam(double[] iprices, HashMap<String,Float> ifmat)
	{
		profit = -1;
		prices = iprices;
		fmat = ifmat;
	}

	/**
	 * Returns profit
	 * @return profit
	 */
	public double get_profit() 
	{
		return profit;
	}
	
	/**
	 * Returns a copy of the prices 
	 * @return prices
	 */
	public double[] get_prices()
	{
		double[] tp = new double[prices.length];
		for (int i=0; i<prices.length; i++) {
			tp[i] = prices[i];
		}
		return tp;
	}

	/**
	 * Returns the number of prices n
	 * @return n
	 */
	public int get_num_prices()
	{
		return prices.length;
	}

	/**
	 * Returns the hashmap fmat containing the fractions of the audience that buy a good 
	 * @return fmat
	 */
	public HashMap<String,Float> get_fmat()
	{
		return fmat;
	}

	/**
	 * Loads model parameters from a file
	 * @param fname
	 * @return parameters loaded from file
	 * @throws Exception
	 */
	public OptParam loadParams(String fname) throws Exception
	{
		File file = new File(fname);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		String[] stprices = new String[0];
		String[] stparam;
		HashMap<String, Float> lfmat = new HashMap<String, Float>();
		int line = 0;
		while ((st = br.readLine()) != null) {
			if (line == 1) {
				stprices = st.split(",");
			}
			else if (line > 3) {
				stparam = st.split(",");
				lfmat.put(stparam[0], Float.parseFloat(stparam[1]));
			}
			line ++;
		}

		double[] lprices = new double[stprices.length];
		for (int i=0; i<stprices.length; i++) {
			lprices[i] = Double.parseDouble(stprices[i]);
		}
		OptParam lparam = new OptParam(lprices,lfmat);
		return lparam;
	}
	
	/**
	 * Update the profit
	 * @param nprofit
	 */
	public void set_profit(double nprofit) 
	{
		profit = nprofit;
	}

	/**
	 * Update the prices
	 * @param nprices
	 */
	public void set_prices(double[] nprices)
	{
		prices = nprices;
	}
}
