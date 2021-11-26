package com.bravenatorsrobotics.freightfrenzy.subsystem;

import com.bravenatorsrobotics.common.operation.OperationMode;

public abstract class AbstractController {

    protected OperationMode<?> operationMode;

    public AbstractController(OperationMode<?> operationMode) {
        this.operationMode = operationMode;
    }

}
