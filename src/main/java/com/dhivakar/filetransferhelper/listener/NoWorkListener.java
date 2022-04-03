package com.dhivakar.filetransferhelper.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NoWorkListener extends StepExecutionListenerSupport {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        if (stepExecution.getReadCount() == 0) {
            log.error("No Batch with Status 'PENDING' in 'BATCH_INFO' Database");
            return ExitStatus.FAILED;
        }
        return null;
    }

}
