package com.example.dingq.typing.Model;

public class SingleTrialResult {
    private boolean timeout;
    private boolean accuracy;
    private int resTime; // 单位为ms

    public SingleTrialResult() {
        timeout = true;
        accuracy = false;
        resTime = 0;
    }

    public SingleTrialResult(boolean cn, int rt) {
        timeout = false;
        accuracy = cn;
        resTime = rt;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public boolean getACC() {
        return accuracy;
    }

    public int getRT() {
        return resTime;
    }
}
