package com.ls.glide_library.progress;

import com.bumptech.glide.load.engine.GlideException;

public interface OnProgressListener {
    void onProgress(String url, long bytesRead, long totalBytes, boolean isDone, GlideException exception);
}
