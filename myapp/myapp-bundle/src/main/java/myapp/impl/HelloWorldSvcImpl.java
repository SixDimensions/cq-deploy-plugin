/*
 * I, the copyright holder of this work, hereby release it into the public domain. This applies worldwide.
 * 
 * In case this is not legally possible, I grant any entity the right to use this work for any purpose, 
 * without any conditions, unless such conditions are required by law.
 */
package myapp.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import myapp.HelloWorldSvc;

/**
 * Implementation of the Hello World Service interface.
 * 
 * @author dklco
 */
@Component(label = "Hello World Service", name = "myapp.impl.HelloWorldSvcImpl", metatype = true, immediate = true)
@Service(value = HelloWorldSvc.class)
@Properties(value = {
		@Property(name = "service.vendor", value = "Six Dimensions"),
		@Property(name = "service.description", value = "Service for greeting the world") })
public class HelloWorldSvcImpl implements HelloWorldSvc {

	/*
	 * (non-Javadoc)
	 * @see myapp.HelloWorldSvc#sayHello()
	 */
	public String sayHello() {
		return "Hello World";
	}

}
