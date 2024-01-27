package io.github.ngspace.nnuedit.asset_manager;

import java.util.EventListener;

@FunctionalInterface
public interface ClearCacheListener extends EventListener {
	public void clearCache();
}
