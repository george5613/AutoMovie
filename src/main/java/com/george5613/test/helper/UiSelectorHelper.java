package com.george5613.test.helper;

public class UiSelectorHelper {

    public static String getSelectKeyByText(String txt){
        StringBuilder builder = new StringBuilder();
        builder.append("new UiSelector().text(\"")
                .append(txt)
                .append("\")");
        return builder.toString();
    }

    public static String getSelectKeyByDescContains(String txt){
        StringBuilder builder = new StringBuilder();
        builder.append("new UiSelector().descriptionContains(\"")
                .append(txt)
                .append("\")");
        return builder.toString();
    }


    public static String getSelectKeyByTextContains(String txt){
        StringBuilder builder = new StringBuilder();
        builder.append("new UiSelector().textContains(\"")
                .append(txt)
                .append("\")");
        return builder.toString();
    }

}
