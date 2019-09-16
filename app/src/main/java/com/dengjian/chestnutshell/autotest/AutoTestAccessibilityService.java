package com.dengjian.chestnutshell.autotest;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class AutoTestAccessibilityService extends AccessibilityService {
    private static final String TAG = "AutoTest";
    private static final String MICROMSG_CHAT_PAGE = "com.tencent.mm.ui.LauncherUI";
    private static final String MICROMSG_MONEY_RECEIVE = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
    private static final String MICROMSG_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if (null != event.getClassName()) {
            Log.d(TAG, "onAccessibilityEvent, packageName = " + event.getClassName().toString());
        }

        switch (eventType) {
            // 通知栏消息
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if (null != texts) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (!TextUtils.isEmpty(content)) {
                            if (content.contains("[微信红包]")) {
                                openWeChatPage(event);
                            }
                        }
                    }
                }
                break;
            // 窗口状态变更
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                String className = null;
                if (null != event.getClassName()) {
                    className = event.getClassName().toString();
                }
                if (MICROMSG_CHAT_PAGE.equals(className)) { // 如果进入了聊天页面
                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                    findMoneyPacket(rootNode);
                }
                break;
        }
    }

    private void findMoneyPacket(AccessibilityNodeInfo rootNode) {
        if (null != rootNode) {
            // 倒序
            for (int i = rootNode.getChildCount() - 1; i >= 0; i--) {
                AccessibilityNodeInfo node = rootNode.getChild(i);
                if (null == node) {
                    continue;
                }
                CharSequence text = node.getText();
                if (null != text && text.toString().equals("微信红包")) {
                    // 往上找可点击的父布局
                    AccessibilityNodeInfo parent = node.getParent();
                    while (null != parent) {
                        if (parent.isCheckable()) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            break;
                        }
                        parent = parent.getParent();
                    }
                }
                findMoneyPacket(node);
            }
        }
    }

    private void openWeChatPage(AccessibilityEvent event) {
        if (null != event.getParcelableData() && event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event.getParcelableData();
            PendingIntent intent = notification.contentIntent;
            try {
                intent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
