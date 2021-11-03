package rmi;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//Remote interface implementation

public class RemoteImpl extends UnicastRemoteObject implements RemoteInterface{

	protected RemoteImpl() throws RemoteException, RMIException, UnknownHostException {
		super();
		 RemoteImpl object = new  RemoteImpl();
		 Skeleton<RemoteInterface> skeleton = new Skeleton <RemoteInterface>(RemoteInterface.class,object);
		 skeleton.start();
		 
		 RemoteInterface stub = Stub.create(RemoteInterface.class, skeleton);
	}

	@Override
	public void testMethod() throws RMIException {
		// TODO Auto-generated method stub
		
	}

}
