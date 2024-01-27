package io.github.ngspace.nnuedit.asset_manager.extensions;

import javax.swing.JComponent;

import io.github.ngspace.nnuedit.App;

public abstract class Extension {public Extension() {}
	/**
	 * Will be executed After Settings and StringTable loaded
	 */
	public void LoadExtension() {}
	/**
	 * Will be executed when the jvm closes
	 */
	public void UnloadExtension() {}
	/**
	 * Ran when App starts Loading
	 * @param app
	 */
	public void PreLoadApplication(App app) {}
	/**
	 * Ran after App finished Loading
	 * @param app
	 */
	public abstract void LoadApplication(App app);
	/**
	 * Ran when app is closed
	 * @param app
	 */
	public abstract void UnloadApplication(App app);
	/**
	 * will build the component that will show up in the Extension tab to represent the extension
	 * @param width - the width of the window
	 * @return the built component
	 */
	public JComponent getOptionsComponent(int width) {return new BasicExtensionPanel(getExtensionValues(),width);}
	/**
	 * Searches <code>ExtensionManager.Extensions</code> for the <code>ExtensionValues</code> of this extension.
	 * @return said ExtensionValues
	 */
	public ExtensionValues getExtensionValues() {
		for (ExtensionValues ext : ExtensionManager.Extensions) if (ext.extension==this) return ext;
		return null;
	}
}
