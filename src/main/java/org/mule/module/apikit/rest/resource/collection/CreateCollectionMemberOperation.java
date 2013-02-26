
package org.mule.module.apikit.rest.resource.collection;

import org.mule.module.apikit.UnexpectedException;
import org.mule.module.apikit.rest.RestException;
import org.mule.module.apikit.rest.RestRequest;
import org.mule.module.apikit.rest.operation.AbstractRestOperation;
import org.mule.module.apikit.rest.operation.RestOperationType;
import org.mule.transport.NullPayload;

import java.net.URI;
import java.net.URISyntaxException;

public class CreateCollectionMemberOperation extends AbstractRestOperation
{

    @Override
    public void handle(RestRequest request) throws RestException
    {
        super.handle(request);
        try
        {
            request.getProtocolAdaptor().handleCreated(
                new URI(request.getProtocolAdaptor().getURI().toString() + "/"
                        + request.getMuleEvent().getFlowVariable(resource.getName() + "Id")), request);
        }
        catch (URISyntaxException e)
        {
            throw new UnexpectedException(e);
        }
        request.getMuleEvent().getMessage().setPayload(NullPayload.getInstance());

    }

    public CreateCollectionMemberOperation()
    {
        this.type = RestOperationType.CREATE;
    }

}
