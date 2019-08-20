package com.dengjian.chestnutshell.imageloader.nutimageloader.loader;

import java.util.HashMap;
import java.util.Map;

public class LoaderManager {
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String FILE = "file";

    private static volatile LoaderManager sInstance = null;

    /**
     *
     */
    private Map<String, Loader> mLoaderMap = new HashMap<String, Loader>();

    private Loader mNullLoader = new NullLoader();

    /**
     *
     */
    private LoaderManager() {
        register(HTTP, new UrlLoader());
        register(HTTPS, new UrlLoader());
        register(FILE, new LocalLoader());
    }

    public static LoaderManager getInstance() {
        if (sInstance == null) {
            synchronized (LoaderManager.class) {
                if (sInstance == null) {
                    sInstance = new LoaderManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * @param schema
     * @param loader
     */
    public final synchronized void register(String schema, Loader loader) {
        mLoaderMap.put(schema, loader);
    }

    public Loader getLoader(String schema) {
        if (mLoaderMap.containsKey(schema)) {
            return mLoaderMap.get(schema);
        }
        return mNullLoader;
    }
}
