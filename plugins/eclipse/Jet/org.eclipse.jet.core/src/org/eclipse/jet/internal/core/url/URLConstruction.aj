package org.eclipse.jet.internal.core.url;

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

    pointcut inURLUtilities() : within(org.eclipse.jet.internal.core.url.URLUtility);

    declare error : newNonEncodingURL() && !inURLUtilities() : "Encoding of URL is suspect. Use or enhance URLUtility instead.";

    declare error: uriToURL() && !inURLUtilities() : "Conversion to URL is suspect. Use URLUtility.toURL(URI) instead.";
    
    declare error: newEncodingURL() && !inURLUtilities() : "Who is using this?";
}
