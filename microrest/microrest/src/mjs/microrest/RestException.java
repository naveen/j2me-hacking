/*

	MicroRest - Rest Library for J2ME

	Copyright (c) 2007 Mary Jane Soft - Marlon J. Manrique
	
	http://mjs.darkgreenmedia.com
	http://marlonj.darkgreenmedia.com

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General
	Public License along with this library; if not, write to the
	Free Software Foundation, Inc., 59 Temple Place, Suite 330,
	Boston, MA  02111-1307  USA

	$Id: RestException.java 227 2007-06-05 07:57:30Z marlonj $
	
*/

package mjs.microrest;

/**
 * REST Exception
 */
public class RestException extends Exception
{
	/**
	 * Create a Rest exception with the message specified
	 *
	 * @param msg Exception message
	 */
	public RestException(String msg)
	{
		super(msg);
	}
}
