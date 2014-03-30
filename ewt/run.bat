@echo off
if "%WTK_HOME%" != "" goto wtk_run
echo Please set WTK_HOME to the Java ME Wireless Toolkit base directory
quit -1
:wtk_run
%WTK_HOME%\bin\emulator.exe -cp microEWT-Examples.jar -Xdescriptor:microEWT-Examples.jad
