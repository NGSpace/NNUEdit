package io.github.ngspace.nnuedit.asset_manager;

import java.util.EventListener;
import java.util.Map;

@FunctionalInterface
public interface AssetLoadedListener extends EventListener {public void loadedAsset(Map<?, ?> cache, Object asset);}
