package org.eclipse.jet.devaspects;

import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;

public aspect URLConstruction {
	pointcut newNonEncodingURL() : call(URL.new(String,String,int,String)) 
							|| call(URL.new(String,String,int,String,URLStreamHandler))
							|| call(URL.new(String,String,String));
	
    pointcut newEncodingURL() : call(URL.new(String))
    || call(URL.new(URL,String))
    || call(URL.new(URL,String,URLStreamHandler));

    pointcut uriToURL() : call(URL URI.toURL());


declare error : newNonEncodingURL() : "Encoding of URL is suspect. Use or enhance URLUtility instead."; //$NON-NLS-1$

//declare error: uriToURL() : "Conversion to URL is suspect. Use URLUtility.toURL(URI) instead."; //$NON-NLS-1$

//declare error : newEncodingURL() : "Call to encoding URL";
}
