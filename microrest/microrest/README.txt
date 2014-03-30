MicroRest Library for J2ME
--------------------------

MicroRest library allows MIDlets to connect and retrive data from rest
services.

Two clases allow the access to the rest services, the RestRequest encapsulate 
the rest request, with the service url, the method and the parameters.
 
The RestResponse provide access to the server response giving access to the 
parts of the response using a xpath like form.

  // Create the request
  RestRequest restRequest = 
  	new RestRequest("http://api.flickr.com/services/rest/");
  restRequest.setParameter("method","flickr.test.echo");
  restRequest.setParameter("value","12345");
  restRequest.setParameter("api_key","xxx");
  
  // Grab the response
  RestResponse restResponse = restRequest.sendAndWait();
  
  // Grab each element value
  println(restResponse.get("/rsp/@stat"));
  println(restResponse.get("/rsp/method/text()"));
  
This class contains the min version of the kXml2 licensed under BSD and 
contains the com.j2medeveloper.util.UrlUtils code licensed under LGPL.

To use the library simply copy the library content to your project.

The examples directory contains examples to show library features.

Enjoy!

http://mjs.darkgreenmedia.com
http://marlonj.darkgreenmedia.com

Copyright (c) 2007 Marlon J. Manrique (marlonj@darkgreenmedia.com)

$Id: README.txt 229 2007-06-05 19:50:46Z marlonj $
