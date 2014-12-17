package com.ubbcn.android.utime.utils;

import com.ubbcn.android.utime.model.ActionModel;

public interface CounterListener {
	void start(ActionModel actionModel);
	void stop(ActionModel actionModel);
}
