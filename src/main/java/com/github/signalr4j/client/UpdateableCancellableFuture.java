/*
Copyright (c) Microsoft Open Technologies, Inc.
All Rights Reserved
See License.txt in the project root for license information.
*/

package com.github.signalr4j.client;

/**
 * An updateable SignalRFuture that, when cancelled, triggers cancellation on an
 * internal instance
 */
public class UpdateableCancellableFuture<V> extends SignalRFuture<V> {
    SignalRFuture<?> mFuture = null;

    Object mSync = new Object();

    public UpdateableCancellableFuture(SignalRFuture<?> token) {
        mFuture = token;
    }

    public void setFuture(SignalRFuture<?> token) {
        synchronized (mSync) {
            mFuture = token;
        }

        if (isCancelled()) {
            if (mFuture != null) {
                mFuture.cancel();
            }
        }
    }

    @Override
    public boolean cancel() {
    	boolean cancelled;
        synchronized (mSync) {
        	cancelled = super.cancel();
            if (mFuture != null) {
                mFuture.cancel();
                mFuture = null;
            }
        }
        return cancelled;
    }
}
