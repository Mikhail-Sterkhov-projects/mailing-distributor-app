package io.itzstonlex.mdapp.mailing;

import java.util.concurrent.atomic.AtomicReference;

public class BufferedMessage<M> {

    private volatile AtomicReference<M> messageRef;

    public void push(M message) {
        if (messageRef == null) {
            messageRef = new AtomicReference<>();
        }
        messageRef.set(message);
    }

    public void delete() {
        if (messageRef != null) {
            messageRef.set(null);
            messageRef = null;
        }
    }

    public M get() {
        if (messageRef == null) {
            return null;
        }
        return messageRef.get();
    }

    public M getAndDelete() {
        var buf = get();
        delete();
        return buf;
    }
}
