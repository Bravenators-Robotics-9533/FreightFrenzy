package com.bravenatorsrobotics.freightfrenzy.subsystem;

import com.bravenatorsrobotics.common.operation.OperationMode;

public abstract class AbstractSubSystemController {

    protected OperationMode<?> operationMode;

    public AbstractSubSystemController(OperationMode<?> operationMode) {
        this.operationMode = operationMode;
    }

}
