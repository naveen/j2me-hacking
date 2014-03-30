Fire v1.2
21-06-2007

Licence: 
Fire-j2me is licenced under the LGPL. This library is distributed in the hope 
that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Lesser General Public License for more details.

Description:
Fire is a lightweight themable GUI engine for j2me MIDP2 applications. It is designed 
to be an eye-candy replacement to the traditional midp GUI components. It provides 
more than the forms and items (midp2) functionality and its not kvm-implementation depended.

Usage:
Check the doc folder for the javadoc and design notes.
You can use the library by adding the compiled classes or source to your project.
Check the ReadMe.java in the gr.bluevibe.fire.test package for a tutorial on using Fire lib.

Demo:
You can run the FireMe.jar sample application (the code is in ReadMe.java) found in the bin 
folder of this package either by using a j2me midp2 phone emulator (like SUN's WTK) or by 
installing it on a mobile phone that supports midp2.

Acknowledgments:

Thanks to everyone who contributed patches and submited bugs, for this release. 
Special thanks to Frank for his bug fixes and contributions and to Maxim Blagov for the motorola fix.


ChangeLog Sing Fire v1.1:

- sizeChanged() bug fixed
- Motorola fix thanks to Maxim Blagov
- bug on setOrientation fixed. 
- Editable row, text constraints bug fix.
- other minor bug fixes (panel scrolling, etc)

- Editable row now can have a fixed height (so it works like a text field)
- Gauge improvement, to require less CPU.
- full highlight when selected for Row and ListElement components
- improved appearence on DateTimeRow component.
- new Movie Component, can present nice animations.





ChangeLog Since Fire v1.0:

- Lots of additions and improvments in the theme. 
Check the fire-theme-specs.txt file in the doc folder, for details on the theme.

- Theme Logo can now be set on LEFT , CENTER, or RIGHT of the top border.

- Softkey commands (on the bottom border) have shortcuts displayed, for easier navigation.

- Fire now supports two landscape modes, lefthanded and righthanded landscape.
It adjusts the joystick and the softkeys accordingly.

- LoadScreen was renamed SplashScreen

- busy mode can now be set to be non-interractive meaning that user input is not allowed while the busyMode is true.
Check the ReadMe.java and the javadoc for more information on non-interractive busy mode.

- When the FireScreen is set to busy mode it will replace the softkey commands with a pre-supplied cancel command.

- lots of bugfixes, and performance improvments.




I hope that you will find this library usefull.
Please send feedback to: padeler@bluebird.gr
