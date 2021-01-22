Version 2 (fixed a major error in version 1)

To compile and run optimization go to src/main/

Compile the code using

	javac OptParam.java Node.java OptProfit.java

To run

	java OptProfit

To change the model parameters modify param.txt maintaining the general format. Its assumed prices are comma separated on line 1 and the comma separated key value pairs of the audience fractions start on line 4. The parameters are not checked to insure that they are valid or complete so enter them carefully. 

In truth this is basically a tech demo and as such needs work to make it practical. For instance explicitly saving the parameters of the model is not efficient when they can be computed on the fly. Yet it makes the algorithm easier to tinker with without modifying any code. 
