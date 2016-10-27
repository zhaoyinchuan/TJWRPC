package com.tianjunwei.socketrpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RPCProxy {

	public RPCProxy(){
		
	}
	
	 public static <T> T create(Object target){
		 
		 return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(), new InvocationHandler(){

			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				 ObjectOutputStream output = new ObjectOutputStream(RPCClient.socket.getOutputStream());  
                 try {  
                	 output.writeUTF(target.getClass().getName());
                     output.writeUTF(method.getName());  
                     output.writeObject(method.getParameterTypes());  
                     output.writeObject(args);  
                     ObjectInputStream input = new ObjectInputStream(RPCClient.socket.getInputStream());  
                     try {  
                         Object result = input.readObject();  
                         if (result instanceof Throwable) {  
                             throw (Throwable) result;  
                         }  
                         return result;  
                     } finally {  
                         input.close();  
                     }  
                 } finally {  
                     output.close();  
                 }  
			}
			 
		 }
		 );
	 }
}
