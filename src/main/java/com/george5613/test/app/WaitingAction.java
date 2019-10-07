package com.george5613.test.app;

import com.george5613.test.app.global.Context;
import com.george5613.test.base.BaseAction;

public class WaitingAction extends BaseAction {

    private static final String TAG = "";

    public WaitingAction(Context context) {
        super(context);
    }

    @Override
    public String getTag() {
        return TAG;
    }

}
