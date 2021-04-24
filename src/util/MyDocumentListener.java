package util;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class MyDocumentListener implements DocumentListener {

	@Override
	public void insertUpdate(DocumentEvent e) {
		onAllUpdates(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		onAllUpdates(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		onAllUpdates(e);
	}

	public abstract void onAllUpdates(DocumentEvent e);
}
