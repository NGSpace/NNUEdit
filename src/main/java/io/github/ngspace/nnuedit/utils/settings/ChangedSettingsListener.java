package io.github.ngspace.nnuedit.utils.settings;

@FunctionalInterface public interface ChangedSettingsListener extends SettingsListener {
	public void changedValue(String key, Object newVal, Object oldval, Settings stng);
}
