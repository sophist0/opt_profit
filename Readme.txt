Version 2 (fixed a major error in version 1)

To compile and run optimization go to src/main/

Compile the code using

	javac OptParam.java Node.java OptProfit.java

To run

	java OptProfit

To change the model parameters modify param.txt maintaining the general format. The parameters are not checked to insure that they are valid or complete so enter them carefully. Alpha is a truncation parameter and sigfig are how many significant figures must be less than alpha for a sub-tree to be truncated. More detailed notes on the ideas behind this code can be found at https://sophist0.github.io/files/pricing_goods.pdf

In truth this is basically a tech demo and as such needs work to make it practical. For instance explicitly saving the parameters of the model is not efficient when they can be computed on the fly. Yet it makes the algorithm easier to tinker with without modifying any code. 
