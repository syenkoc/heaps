/*  
 * $Id$  
 *   
 * Copyright (c) 2012-2014 Fran Lattanzio  
 *   
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal  
 * in the Software without restriction, including without limitation the rights  
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
 * copies of the Software, and to permit persons to whom the Software is  
 * furnished to do so, subject to the following conditions:  
 *   
 * The above copyright notice and this permission notice shall be included in  
 * all copies or substantial portions of the Software.  
 *   
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  
 * SOFTWARE.  
 */ 
package org.teneighty.heap;


/**
 * Object utility methods that should already be implemented in the core Java framework somewhere.
 * <p>
 * This class is stateless and cannot be instantiated.
 */
public final class Objects
{
	
	/**
	 * Compare two objects for equality, considering <code>null</code> values.
	 * 
	 * @param o1 the first object.
	 * @param o2 the second object.
	 * @return <code>true</code> if <code>o1</code> is equal to <code>o2</code>;
	 *         <code>false</code> otherwise.
	 */
	public static boolean objectEquals(final Object o1, final Object o2)
	{
		return (o1 == null) ? (o2 == null) : o1.equals(o2);
	}

	/**
	 * Get the hashcode for the specified Object, or 0 if <code>o</code> is
	 * <code>null</code>.
	 * 
	 * @param anObject the Object for which to get a hashcode.
	 * @return the hashcode or 0 if <code>o</code> is <code>null</code>.
	 */
	public static int objectHashCode(final Object anObject)
	{
		return (anObject == null) ? 0 : anObject.hashCode();
	}
	
	/**
	 * Here only for access protection.
	 */
	private Objects()
	{
	}

}
