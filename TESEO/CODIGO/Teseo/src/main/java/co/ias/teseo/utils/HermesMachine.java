/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ias.teseo.utils;


public class HermesMachine {
        /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
     * @return method name
     */
    public static String getMethodName(final int depth)
    {
      final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

      return ste[ste.length - 1 - depth].getMethodName();
    }
    
    public static boolean isThereNull(String... vars)
    {
    	for(String var : vars)
    		if(var == null)
    			return true;
    	return false;
    }
}
