import java.util.Arrays;
import java.util.HashMap;
import java.io.*;

public class OptParam {

	private int sigfig;
	private double alpha;
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
	public OptParam(double[] iprices, HashMap<String,Float> ifmat, int isigfig, double ialpha)
	{
		profit = -1;
		prices = iprices;
		fmat = ifmat;
		sigfig = isigfig;
		alpha = ialpha;
	}
	
	/**
	 * Returns alpha
	 * @return profit
	 */
	public double get_alpha() 
	{
		return alpha;
	}
	
	/**
	 * Returns sigfig
	 * @return sigfig
	 */
	public int get_sigfig() 
	{
		return sigfig;
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
		double[] lprices = new double[0];	
		File file = new File(fname);	
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String st;
		String[] stparam;
		int lsigfig = 10;
		double lalpha = 0;
		HashMap<String, Float> lfmat = new HashMap<String, Float>();
		int line = 0;
		try {
			while ((st = br.readLine()) != null) {
				if (line == 1) {
					String[] stprices = st.split(",");
					lprices = new double[stprices.length-1];
					for (int i=1; i<stprices.length; i++) {
						lprices[i-1] = Double.parseDouble(stprices[i]);
					}
				}
				else if (line == 2) {
					stparam = st.split(",");
					lsigfig = Integer.parseInt(stparam[1]);
				}
				else if (line == 3) {
					stparam = st.split(",");
					lalpha = Double.parseDouble(stparam[1]);	
				}
				else if (line > 0) {
					stparam = st.split(",");
					String[] tk = stparam[0].split("\\|");
					String key = "";
					for (int i=0; i<tk.length; i++) {
						key += tk[i];
					}
					lfmat.put(key, Float.parseFloat(stparam[1]));
				}
				line ++;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		OptParam lparam = new OptParam(lprices,lfmat,lsigfig,lalpha);
		return lparam;
	}
	
	/**
	 * Update the profit
	 * @param nprofit
	 */
	public void set_fmat(HashMap<String,Float> nfmat) 
	{
		fmat = nfmat;
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
