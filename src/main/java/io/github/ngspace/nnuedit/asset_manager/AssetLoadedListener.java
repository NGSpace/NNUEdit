package io.github.ngspace.nnuedit.asset_manager;

import java.util.EventListener;
import java.util.Map;

@FunctionalInterface
public interface AssetLoadedListener extends EventListener {

	public void LoadedAsset(Map<Object, Object> cache, Object asset);
}
