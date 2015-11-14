package com.fortmocks.mock.soap.model.project.processor;

import com.fortmocks.core.model.Processor;
import com.fortmocks.core.model.Result;
import com.fortmocks.core.model.Task;
import com.fortmocks.mock.soap.model.project.domain.SoapOperation;
import com.fortmocks.mock.soap.model.project.domain.SoapPort;
import com.fortmocks.mock.soap.model.project.domain.SoapProject;
import com.fortmocks.mock.soap.model.project.dto.SoapProjectDto;
import com.fortmocks.mock.soap.model.project.processor.message.input.CreateSoapPortsInput;
import com.fortmocks.mock.soap.model.project.processor.message.input.CreateSoapProjectInput;
import com.fortmocks.mock.soap.model.project.processor.message.output.CreateSoapPortsOutput;
import com.fortmocks.mock.soap.model.project.processor.message.output.CreateSoapProjectOutput;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
@Service
public class CreateSoapPortsProcessor extends AbstractSoapProjectProcessor implements Processor<CreateSoapPortsInput, CreateSoapPortsOutput> {

    /**
     * The process message is responsible for processing an incoming task and generate
     * a response based on the incoming task input
     * @param task The task that will be processed by the processor
     * @return A result based on the processed incoming task
     * @see Task
     * @see Result
     */
    @Override
    public Result<CreateSoapPortsOutput> process(final Task<CreateSoapPortsInput> task) {
        final CreateSoapPortsInput input = task.getInput();
        final SoapProject soapProject = findType(input.getSoapProjectId());
        final List<SoapPort> soapPortTypes = toDtoList(input.getSoapPorts(), SoapPort.class);

        for(SoapPort newSoapPort : soapPortTypes){
            SoapPort existingSoapPort = findSoapPortWithName(soapProject, newSoapPort.getName());

            if(existingSoapPort == null){
                generateId(newSoapPort);
                soapProject.getSoapPorts().add(newSoapPort);
                continue;
            }

            final LinkedList<SoapOperation> soapOperations = new LinkedList<SoapOperation>();
            for(SoapOperation newSoapOperation : newSoapPort.getSoapOperations()){
                SoapOperation existingSoapOperation = findSoapOperationWithName(existingSoapPort, newSoapOperation.getName());

                if(existingSoapOperation != null){
                    existingSoapOperation.setOriginalEndpoint(newSoapOperation.getOriginalEndpoint());
                    existingSoapOperation.setSoapOperationType(newSoapOperation.getSoapOperationType());
                    soapOperations.add(existingSoapOperation);
                } else {
                    generateId(newSoapOperation);
                    soapOperations.add(newSoapOperation);
                }
            }
            existingSoapPort.setSoapOperations(soapOperations);
        }

        save(soapProject);
        return createResult(new CreateSoapPortsOutput());
    }
}
