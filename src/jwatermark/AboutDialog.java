package jwatermark;

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

		String msg = uiText[8] + " " + uiText[9] + " <" + uiText[10] + ">\n\n" + 
					 uiText[11] + "<a>" + uiText[12] + "</a>";
		
		org.eclipse.swt.widgets.Link label = new org.eclipse.swt.widgets.Link(aboutShell, SWT.NONE);
		label.setText(msg);
		label.setBounds(10, 20, 290, 50);
//		label.setFont(new org.eclipse.swt.graphics.Font(display, "Tahhoma", 11, SWT.NORMAL));
		
		Button button = new Button(aboutShell, SWT.PUSH);
		button.setText(uiText[13]);
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
