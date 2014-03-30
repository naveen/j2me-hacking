SiRFstudio api - release 7/18/2007
-------------------------------------------

This release is intended for evaluation only.

This package contains two versions of the API and demo applications:
- \bluetooth\*
	Bluetooth version of the library and applications.
	Dependencies: cLCD1.1, MIDP20, JSR120 Wireless Messaging API 1.1, JSR82 Bluetooth API, transparency support on the device used
	Device tested on: Motorola V3X
- \nonBluetooth(J9)
	Non-Bluetooth version of the library and applications.
	Dependencies: cLCD1.1, MIDP20
	Device tested on: Mio PPC running J9 JVM


In above folders:
- api\SiRFstudioAPI.jar
	The library. Include this in your projects. 
- demo\APIDemoMidlet.java
	Sample code demonstrating how to use the API.
- demo\DemoMidlet.jar & .jad
	MIDlet suite containing the following MIDlets:
	- APIDemoMidlet: The above sample code built as a Midlet.
	
Also:
- \javadocs
	Javadocs for the API in html and PDF formats.


