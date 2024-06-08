package io.github.ngspace.nnuedit.utils.settings;

@FunctionalInterface
public interface RefreshListener extends ChangedSettingsListener  {
	public default void changedValue(String key, Object newVal, Object oldval, Settings stng) {refresh(stng);}
	public void refresh(Settings stng);
}
