package io.github.ngspace.nnuedit.utils;

import io.github.ngspace.nnuedit.utils.settings.Settings;

public class VersionInfo {
	private Settings info = new Settings(Utils.getAssetAsStream("NNUEditVersion.properties"));
	public double versionnumber;
	public String platform;
	public String version;
	public VersionInfo() {
		versionnumber = info.getDouble("VersionNumber");
		platform = info.get("Platform");
		version = info.get("Version");
	}
}
