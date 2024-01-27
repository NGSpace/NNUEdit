package io.github.ngspace.nnuedit.asset_manager.extensions;

import java.util.Map;

public class ExtensionValues {
	public final Extension extension;
	public final ClassLoader extensionLoader;
	public final Map<String, Object> configuration;
	public ExtensionValues(Extension extension, ClassLoader extensionLoader, Map<String, Object> map) {
		this.extension = extension;
		this.configuration = map;
		this.extensionLoader = extensionLoader;
	}
}
