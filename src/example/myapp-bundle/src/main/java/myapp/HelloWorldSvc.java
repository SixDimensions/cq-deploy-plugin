/*
 * I, the copyright holder of this work, hereby release it into the public domain. This applies worldwide.
 * 
 * In case this is not legally possible, I grant any entity the right to use this work for any purpose, 
 * without any conditions, unless such conditions are required by law.
 */
package myapp;

/**
 * Service interface for saying hello to the world.
 * 
 * @author dklco
 */
public interface HelloWorldSvc {

	/**
	 * Method for saying hello, should return "Hello World".
	 * 
	 * @return "Hello World"
	 */
	public String sayHello();
}
