/*
Copyright (c) 2011, Carlos Tse <copperoxide@gmail.com>
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package jwatermark;

import static jwatermark.Constant.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class AboutDialog {

	private static final Point SHELL_SIZE = new Point(325, 160);

	public AboutDialog(Display display, Image icon, String[] uiText) {
		super();
		this.display = display;
		this.icon = icon;
		this.uiText = uiText;
	}

	private Display display;
	private Image icon;
	private String[] uiText;

	public void show(){
		Shell aboutShell = new Shell(display, SWT.DIALOG_TRIM);
		aboutShell.setText(uiText[0] + " " + uiText[1]);
		aboutShell.setImage(icon);

		String msg = uiText[ABT_MSG_ST] + " " + uiText[ABT_MSG_ST + 1] + " <" + uiText[ABT_MSG_ST + 2] + ">\n\n" + 
		uiText[ABT_MSG_ST + 3] + "<a>" + uiText[ABT_MSG_ST + 4] + "</a>";

		org.eclipse.swt.widgets.Link label = new org.eclipse.swt.widgets.Link(aboutShell, SWT.NONE);
		label.setText(msg);
		label.setBounds(10, 20, 290, 50);
		//		label.setFont(new org.eclipse.swt.graphics.Font(display, "Tahhoma", 11, SWT.NORMAL));

		Button button = new Button(aboutShell, SWT.PUSH);
		button.setText(uiText[ABT_MSG_ST + 5]);
		button.setBounds(SHELL_SIZE.x - 20 - 80, SHELL_SIZE.y - 50- 25, 80, 25);
		button.addListener(SWT.Selection, new aboutCloseListener());

		aboutShell.setSize(SHELL_SIZE); 
		aboutShell.setMinimumSize(SHELL_SIZE);
		aboutShell.open();

		while(!aboutShell.isDisposed())
			if (!display.readAndDispatch()) 
				display.sleep();
	}

	private static class aboutCloseListener implements Listener {
		public void handleEvent(Event event) {
			Button widget = (Button) event.widget;
			widget.getShell().dispose();
		}
	}
}
