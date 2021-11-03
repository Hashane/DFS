package rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Proxy;

/** RMI stub factory.

    <p>
    RMI stubs hide network communication with the remote server and provide a
    simple object-like interface to their users. This class provides methods for
    creating stub objects dynamically, when given pre-defined interfaces.

    <p>
    The network address of the remote server is set when a stub is created, and
    may not be modified afterwards. Two stubs are equal if they implement the
    same interface and carry the same remote server address - and would
    therefore connect to the same skeleton. Stubs are serializable.
 */
public abstract class Stub
{
    /** Creates a stub, given a skeleton with an assigned adress.

        <p>
        The stub is assigned the address of the skeleton. The skeleton must
        either have been created with a fixed address, or else it must have
        already been started.

        <p>
        This method should be used when the stub is created together with the
        skeleton. The stub may then be transmitted over the network to enable
        communication with the skeleton.

        @param c A <code>Class</code> object representing the interface
                 implemented by the remote object.
        @param skeleton The skeleton whose network address is to be used.
        @return The stub created.
        @throws IllegalStateException If the skeleton has not been assigned an
                                      address by the user and has not yet been
                                      started.
        @throws UnknownHostException When the skeleton address is a wildcard and
                                     a port is assigned, but no address can be
                                     found for the local host.
        @throws NullPointerException If any argument is <code>null</code>.
        @throws Error If <code>c</code> does not represent a remote interface
                      - an interface in which each method is marked as throwing
                      <code>RMIException</code>, or if an object implementing
                      this interface cannot be dynamically created.
     */
    public static <T> T create(Class<T> c, Skeleton<T> skeleton)
    		throws UnknownHostException
    {
    	 // NullException
        if (c == null || skeleton == null) {
        	throw new NullPointerException("Null arguments");
        }
        
        //checking if is a interface  
        Method[] mthds = c.getDeclaredMethods(); //returns an array of Method objects
   	 
        for (int i=0;i<mthds.length;i++) {  //looping the array
       	 
            Class[] exceptions = mthds[i].getExceptionTypes(); //types of exceptions declared to be thrown
            
            //creating a List out of the array
            List<Class> exceptionList = Arrays.asList(exceptions);
            
            
            if (!(exceptionList.contains(RMIException.class)) || !(c.isInterface())) {
                throw new Error("Object c is not a remote interface");
            }
        }
        
        
        T response = (T) Proxy.newProxyInstance(c.getClassLoader(), new Class<?> [] { c }, new DynamicProxy<T>(c,skeleton));
        
		return response;
    }

    /** Creates a stub, given a skeleton with an assigned address and a hostname
        which overrides the skeleton's hostname.

        <p>
        The stub is assigned the port of the skeleton and the given hostname.
        The skeleton must either have been started with a fixed port, or else
        it must have been started to receive a system-assigned port, for this
        method to succeed.

        <p>
        This method should be used when the stub is created together with the
        skeleton, but firewalls or private networks prevent the system from
        automatically assigning a valid externally-routable address to the
        skeleton. In this case, the creator of the stub has the option of
        obtaining an externally-routable address by other means, and specifying
        this hostname to this method.

        @param c A <code>Class</code> object representing the interface
                 implemented by the remote object.
        @param skeleton The skeleton whose port is to be used.
        @param hostname The hostname with which the stub will be created.
        @return The stub created.
        @throws IllegalStateException If the skeleton has not been assigned a
                                      port.
        @throws NullPointerException If any argument is <code>null</code>.
        @throws Error If <code>c</code> does not represent a remote interface
                      - an interface in which each method is marked as throwing
                      <code>RMIException</code>, or if an object implementing
                      this interface cannot be dynamically created.
     */
    public static <T> T create(Class<T> c, Skeleton<T> skeleton,
                               String hostname)
    {
    	// NullException
        if (c == null || skeleton == null || hostname == null) {
        	throw new NullPointerException("Null arguments");
        }
        
        //checking if is a interface  
   	 	Method[] mthds = c.getDeclaredMethods(); //returns an array of Method objects
   	 
        for (int i=0;i<mthds.length;i++) {  //looping the array
       	 
            Class[] exceptions = mthds[i].getExceptionTypes(); //types of exceptions declared to be thrown
            
            //creating a List out of the array
            List<Class> exceptionList = Arrays.asList(exceptions);
            
            
            if (!(exceptionList.contains(RMIException.class)) || !(c.isInterface())) {
                throw new Error("Object c is not a remote interface");
            }
        }
        
       
        T response = (T) Proxy.newProxyInstance(c.getClassLoader(), new Class<?> [] { c }, new DynamicProxy<T>(c, new InetSocketAddress(hostname,skeleton.addr.getPort())));
        
		return response;
    }

    /** Creates a stub, given the address of a remote server.

        <p>
        This method should be used primarily when bootstrapping RMI. In this
        case, the server is already running on a remote host but there is
        not necessarily a direct way to obtain an associated stub.

        @param c A <code>Class</code> object representing the interface
                 implemented by the remote object.
        @param address The network address of the remote skeleton.
        @return The stub created.
        @throws NullPointerException If any argument is <code>null</code>.
        @throws Error If <code>c</code> does not represent a remote interface
                      - an interface in which each method is marked as throwing
                      <code>RMIException</code>, or if an object implementing
                      this interface cannot be dynamically created.
     */
    public static <T> T create(Class<T> c, InetSocketAddress address)
    {
    	// NullException
        if (c == null || address == null ) {
            throw new NullPointerException("Null arguments");
        }
        
        //checking if is a interface  
   	 	Method[] mthds = c.getDeclaredMethods(); //returns an array of Method objects
   	 
        for (int i=0;i<mthds.length;i++) {  //looping the array
       	 
            Class[] exceptions = mthds[i].getExceptionTypes(); //types of exceptions declared to be thrown
            
            //creating a List out of the array
            List<Class> exceptionList = Arrays.asList(exceptions);
            
            
            if (!(exceptionList.contains(RMIException.class)) || !(c.isInterface())) {
                throw new Error("Object c is not a remote interface");
            }
        }
        
        T response = (T) Proxy.newProxyInstance(c.getClassLoader(), new Class<?> [] { c }, new DynamicProxy<T>(c,address));
        
        
		return response;
    }
    
  
}

class DynamicProxy<T> implements InvocationHandler{
	InetAddress address;
    int port;
    Class<T> c;
	
	
	//Constructors 
	public DynamicProxy(Class<T> c,Skeleton<T> skeleton) {
		this.address = skeleton.addr.getAddress();
        this.port = skeleton.addr.getPort();
        this.c = c;
	}
	
	public DynamicProxy(Class<T> c,InetSocketAddress socketAddress) {
		this.address = socketAddress.getAddress();
		this.port = socketAddress.getPort();
        this.c = c;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
	 Socket socket = null;
 
     ObjectOutputStream output = null;
     ObjectInputStream	input = null;
     
    	
     //proxy(Stub Object) connects to the corresponding skeleton at the server side in order to invoke methods and receive response
     
     socket = new Socket(address,port);
     output = new ObjectOutputStream(socket.getOutputStream());
     output.flush();
     
     //invoking methods
     output.writeObject(method);
    
     
     //receiving response 
     input = new ObjectInputStream(socket.getInputStream());
     Object response =  input.read();	
     
     try {
	     input.close();
	     output.close();
	     socket.close();
     }catch(IOException e) {
    	 e.printStackTrace();
     }
		
	 return response;
	}
	
}
