package com.bravenatorsrobotics.common.linearstatemachine;

import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class LinearState {

    public final String stateName;

    public LinearState(String stateName) {
        this.stateName = stateName;
    }

    public abstract void RunState();

}
