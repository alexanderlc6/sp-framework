/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.remoting.httpinvoker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * Abstract base implementation of the HttpInvokerRequestExecutor interface.
 *
 * <p>Pre-implements serialization of RemoteInvocation objects and
 * deserialization of RemoteInvocationResults objects.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see #doExecuteRequest
 */
public abstract class AbstractHttpInvokerRequestExecutor {
	
	private static final int SERIALIZED_INVOCATION_BYTE_ARRAY_INITIAL_SIZE = 1024;
	
	/**
	 * Serialize the given RemoteInvocation into a ByteArrayOutputStream.
	 * @param invocation the RemoteInvocation object
	 * @return a ByteArrayOutputStream with the serialized RemoteInvocation
	 * @throws IOException if thrown by I/O methods
	 */
	protected ByteArrayOutputStream getByteArrayOutputStream(RemoteInvocation invocation) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(SERIALIZED_INVOCATION_BYTE_ARRAY_INITIAL_SIZE);
		writeRemoteInvocation(invocation, baos);
		return baos;
	}
	
	/**
	 * Serialize the given RemoteInvocation to the given OutputStream.
	 * <p>The default implementation gives {@code decorateOutputStream} a chance
	 * to decorate the stream first (for example, for custom encryption or compression).
	 * Creates an {@code ObjectOutputStream} for the final stream and calls
	 * {@code doWriteRemoteInvocation} to actually write the object.
	 * <p>Can be overridden for custom serialization of the invocation.
	 * @param invocation the RemoteInvocation object
	 * @param os the OutputStream to write to
	 * @throws IOException if thrown by I/O methods
	 * @see #decorateOutputStream
	 * @see #doWriteRemoteInvocation
	 */
	protected void writeRemoteInvocation(RemoteInvocation invocation, OutputStream os) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(decorateOutputStream(os));
		try {
			doWriteRemoteInvocation(invocation, oos);
		}
		finally {
			oos.close();
		}
	}
	
	/**
	 * Perform the actual writing of the given invocation object to the
	 * given ObjectOutputStream.
	 * <p>The default implementation simply calls {@code writeObject}.
	 * Can be overridden for serialization of a custom wrapper object rather
	 * than the plain invocation, for example an encryption-aware holder.
	 * @param invocation the RemoteInvocation object
	 * @param oos the ObjectOutputStream to write to
	 * @throws IOException if thrown by I/O methods
	 * @see ObjectOutputStream#writeObject
	 */
	protected void doWriteRemoteInvocation(RemoteInvocation invocation, ObjectOutputStream oos) throws IOException {
		oos.writeObject(invocation);
	}

	/**
	 * Return the OutputStream to use for writing remote invocations,
	 * potentially decorating the given original OutputStream.
	 * <p>The default implementation returns the given stream as-is.
	 * Can be overridden, for example, for custom encryption or compression.
	 * @param os the original OutputStream
	 * @return the potentially decorated OutputStream
	 */
	protected OutputStream decorateOutputStream(OutputStream os) throws IOException {
		return os;
	}


	/**
	 * Perform the actual reading of an invocation object from the
	 * given ObjectInputStream.
	 * <p>The default implementation simply calls {@code readObject}.
	 * Can be overridden for deserialization of a custom wrapper object rather
	 * than the plain invocation, for example an encryption-aware holder.
	 * @param ois the ObjectInputStream to read from
	 * @return the RemoteInvocationResult object
	 * @throws IOException if thrown by I/O methods
	 * @throws ClassNotFoundException if the class name of a serialized object
	 * couldn't get resolved
	 * @see ObjectOutputStream#writeObject
	 */
	protected RemoteInvocationResult doReadRemoteInvocationResult(ObjectInputStream ois)
			throws IOException, ClassNotFoundException {

		Object obj = ois.readObject();
		if (!(obj instanceof RemoteInvocationResult)) {
			throw new RemoteException("Deserialized object needs to be assignable to type [" +
					RemoteInvocationResult.class.getName() + "]: " + obj);
		}
		return (RemoteInvocationResult) obj;
	}
	
	/**
	 * Execute a request to send the given serialized remote invocation.
	 * <p>Implementations will usually call {@code readRemoteInvocationResult}
	 * to deserialize a returned RemoteInvocationResult object.
	 * @param config the HTTP invoker configuration that specifies the
	 * target service
	 * @param baos the ByteArrayOutputStream that contains the serialized
	 * RemoteInvocation object
	 * @return the RemoteInvocationResult object
	 * @throws IOException if thrown by I/O operations
	 * @throws ClassNotFoundException if thrown during deserialization
	 * @throws Exception in case of general errors
	 * @see #readRemoteInvocationResult(java.io.InputStream, String)
	 */
	protected abstract RemoteInvocationResult doExecuteRequest(
			HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
			throws Exception;
}