package org.flowvisor.api;

import java.util.HashMap;

import org.flowvisor.api.handlers.AddSlice;
import org.flowvisor.api.handlers.ApiHandler;
import org.flowvisor.api.handlers.ListSlices;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;
import com.thetransactioncompany.jsonrpc2.server.RequestHandler;

public class ConfigurationHandler implements RequestHandler {

	@SuppressWarnings( { "serial", "rawtypes" } )
	HashMap<String, ApiHandler> handlers = new HashMap<String, ApiHandler>() {{
		put("list-slices", new ListSlices());
		put("add-slice", new AddSlice());
	}};
	
	private final String[] methods =  { "list-slices"}; 
	
	@Override
	public String[] handledRequests() {
		return methods;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctxt) {
		ApiHandler m = handlers.get(req.getMethod());
		if (m != null) {
			
			switch (req.getParamsType()) {
			case NO_PARAMS:
				return m.process(null);
			case ARRAY:
				return m.process(req.getPositionalParams());
			case OBJECT:
				return m.process(req.getNamedParams());
			}
		} 
		return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
	}

}
