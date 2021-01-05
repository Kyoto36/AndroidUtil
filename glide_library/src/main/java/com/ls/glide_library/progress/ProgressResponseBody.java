package com.ls.glide_library.progress;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @ClassName: ProgressReponseBody
 * @Description:
 * @Author: ls
 * @Date: 2020/12/29 19:36
 */
public class ProgressResponseBody extends ResponseBody {

    private String mUrl;
    private ResponseBody mResponseBody;
    private OnProgressListener mListener;
    private BufferedSource mBufferedSource;

    public ProgressResponseBody(String url, ResponseBody responseBody, OnProgressListener listener) {
        mUrl = url;
        mResponseBody = responseBody;
        mListener = listener;
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @NotNull
    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0;
            float lastPPM = 0;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                long totalLength = contentLength();
                totalBytesRead += (bytesRead == -1) ? 0 : bytesRead;
                float ppm = (float) (totalBytesRead / (totalLength / 10000.0));

                if (mListener != null && (lastPPM == 0 || ppm - lastPPM > 10 || totalBytesRead == totalLength)) {
                    lastPPM = ppm;
                    mListener.onProgress(mUrl, totalBytesRead, totalLength, (bytesRead == -1), null);
                }
                return bytesRead;
            }
        };
    }
}
