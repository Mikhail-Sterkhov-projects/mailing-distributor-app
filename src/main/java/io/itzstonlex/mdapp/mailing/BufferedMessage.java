package io.itzstonlex.mdapp.mailing;

import java.util.concurrent.atomic.AtomicReference;

public class BufferedMessage<M> {

    private AtomicReference<M> messageRef;

    public synchronized void push(M message) {
        if (messageRef == null) {
            messageRef = new AtomicReference<>();
        }
        messageRef.set(message);
    }

    public synchronized void delete() {
        if (messageRef != null) {
            messageRef.set(null);
            messageRef = null;
        }
    }

    public synchronized M get() {
        if (messageRef == null) {
            return null;
        }
        return messageRef.get();
    }

    public synchronized M getAndDelete() {
        var buf = get();
        delete();
        return buf;
    }
}
