package org.omegat.plugins.sessionlog.instrumented;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.omegat.gui.editor.EditorTextArea3;
import org.omegat.gui.editor.autocompleter.AbstractAutoCompleterView;
import org.omegat.gui.editor.autocompleter.AutoCompleter;
import org.omegat.gui.editor.autocompleter.AutoCompleterItem;
import org.omegat.plugins.sessionlog.SessionLogPlugin;
import org.omegat.util.StaticUtils;

public class InstrumentedAutoComplete extends AutoCompleter {

	final String AUTOCOMPLETE_ACTION = "autocompleteAction";
	// Do not log show/hide/update if this is true.
	public static boolean reducedLog = false;

	public static EditorTextArea3 getEditor(AutoCompleter c) {
		Field f;
		EditorTextArea3 editor = null;
		try {
			f = AutoCompleter.class.getField("editor");
			f.setAccessible(true);
			editor = (EditorTextArea3) f.get(c);
		} catch (NoSuchFieldException | IllegalAccessException ex) {
			System.out.println(
					"SessionLog error: getting the editor " + ex.getMessage());
		}
		return editor;
	}

	public InstrumentedAutoComplete(AutoCompleter c, EditorTextArea3 editor) {
		super(editor);
		Field f, f2;
		List<AbstractAutoCompleterView> views;
		try {
			f = AutoCompleter.class.getDeclaredField("views");
			f.setAccessible(true);
			views = (List<AbstractAutoCompleterView>) f.get(c);
			// f2 = InstrumentedAutoComplete.class.getDeclaredField("views");
			f.set(this, views);
		} catch (NoSuchFieldException | IllegalAccessException ex) {
			System.out.println("SessionLog error: getting the editor list "
					+ ex.getMessage());
		}
	}

	@Override
	public boolean processKeys(KeyEvent e) {
		if (StaticUtils.isKey(e, KeyEvent.VK_UP, 0)) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_UP", "", "");
		}
		if (StaticUtils.isKey(e, KeyEvent.VK_DOWN, 0)) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_DOWN", "", "");
		}
		if (StaticUtils.isKey(e, KeyEvent.VK_RIGHT, 0)) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_RIGHT", "", "");
		}
		if (StaticUtils.isKey(e, KeyEvent.VK_LEFT, 0)) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_LEFT", "", "");
		}
		if (StaticUtils.isKey(e, KeyEvent.VK_TAB, 0)) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_TAB", "", "");
		}
		if (StaticUtils.isKey(e, KeyEvent.VK_PAGE_UP, 0)) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_PAGE_UP", "", "");
		}
		if (StaticUtils.isKey(e, KeyEvent.VK_PAGE_DOWN, 0)) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_PAGE_DOWN", "", "");
		}
		if (StaticUtils.isKey(e, KeyEvent.VK_HOME,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
				|| StaticUtils.isKey(e, KeyEvent.VK_HOME, 0)) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_HOME", "", "");
		}
		if (StaticUtils.isKey(e, KeyEvent.VK_END,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
				|| StaticUtils.isKey(e, KeyEvent.VK_END, 0)) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_END", "", "");
		}
		if ((StaticUtils.isKey(e, KeyEvent.VK_ENTER, 0))) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_ENTER", "", "");
		}
		if ((StaticUtils.isKey(e, KeyEvent.VK_INSERT, 0))) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_INSERT", "", "");
		}
		if ((StaticUtils.isKey(e, KeyEvent.VK_ESCAPE, 0))) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_ESCAPE", "", "");
		}
		if (StaticUtils.isKey(e, KeyEvent.VK_RIGHT,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_RIGHT", "", "");
		}

		if (StaticUtils.isKey(e, KeyEvent.VK_LEFT,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"VK_LEFT", "", "");
		}
		return super.processKeys(e);
	}

	@Override
	public void doSelection() {
		AutoCompleterItem item = null;
		Method m = null;
		StringBuilder extras = new StringBuilder();

		try {
			m = AutoCompleter.class.getDeclaredMethod("getSelectedValue");
			m.setAccessible(true);
			item = (AutoCompleterItem) m.invoke(this);
		} catch (Exception ex) {
			System.out.println("SessionLog error: logging selection");
		}

		if (item != null) {
			if (item.extras != null) {
				for (String s : item.extras) {
					extras.append(s);
					extras.append(",");
				}
			}

			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"DO_SELECTION", extras.toString(), item.payload);
		}
		super.doSelection();
	}

	@Override
	public void updatePopup() {
		if (!reducedLog) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"UPDATE", "", "");
		}
		try {
			super.updatePopup();
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	@Override
	public void setVisible(boolean isVisible) {
		if (!reducedLog) {
			if (isVisible)
				SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
						"SHOW", "", "");
			else
				SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
						"HIDE", "", "");
		}
		super.setVisible(isVisible);
	}

	@Override
	public void textDidChange() {
		if (!reducedLog) {
			SessionLogPlugin.getLogger().GenericEvent(AUTOCOMPLETE_ACTION,
					"TEXTCHANGE", "", "");
		}
		super.textDidChange();
	}
}
