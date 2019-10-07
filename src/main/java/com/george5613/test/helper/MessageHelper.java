package com.george5613.test.helper;

import com.george5613.test.app.taopp.model.Seat;
import javafx.util.Pair;

public class MessageHelper {

    public static String getSmsMsg(String title) {
        StringBuilder builder = new StringBuilder();
        builder.append("大佬,")
                .append("您想要的看的电影")
                .append("<")
                .append(title)
                .append(">,")
                .append("电影票已经锁座成功,请在10分钟内登录淘票票App进行支付购买");
        return builder.toString();
    }
}
