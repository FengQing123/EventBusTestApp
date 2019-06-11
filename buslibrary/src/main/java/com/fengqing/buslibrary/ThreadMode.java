package com.fengqing.buslibrary;

/**
 * @author fengqing
 * @date 2019/6/10
 */

public enum ThreadMode {
    /**
     * Subscriber will be called in the same thread, which is posting the event. This is the default. Event delivery
     * implies the least overhead because it avoids thread switching completely. Thus this is the recommended mode for
     * simple tasks that are known to complete is a very short time without requiring the main thread. Event handlers
     * using this mode must return quickly to avoid blocking the posting thread, which may be the main thread.
     */
    POSTING,

    /**
     * Subscriber will be called in Android's main thread (sometimes referred to as UI thread). If the posting thread is
     * the main thread, event handler methods will be called directly. Event handlers using this mode must return
     * quickly to avoid blocking the main thread.
     */
    MAIN,

    /**
     * Subscriber will be called in a background thread. If posting thread is not the main thread, event handler methods
     * will be called directly in the posting thread. If the posting thread is the main thread, EventBus uses a single
     * background thread, that will deliver all its events sequentially. Event handlers using this mode should try to
     * return quickly to avoid blocking the background thread.
     */
    BACKGROUND,

}
