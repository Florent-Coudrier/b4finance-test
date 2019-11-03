/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.resource;

import b4finance.test.api.error.ClientError;
import b4finance.test.api.error.ErrorCode;
import b4finance.test.api.error.SystemException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
public class AppExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
    
    @Context
	private HttpHeaders headers;

	@Override
	public Response toResponse(Exception exception) {
		if (exception instanceof SystemException) {
			SystemException systemException = (SystemException) exception;
			ClientError clientError = new ClientError();
			clientError.setCode(systemException.getErrorCode().toString());
			clientError.setMessage(systemException.getErrorCode().getMessage());
			clientError.setDetail(systemException.getErrorDetail());
			return formatResponse(clientError, systemException.getErrorCode().getStatus());
		}
		else if (exception instanceof WebApplicationException) {
			WebApplicationException webApplicationException = (WebApplicationException) exception;
			int code = webApplicationException.getResponse().getStatus();
			Response.Status status = Response.Status.fromStatusCode(code);
			ClientError clientError = new ClientError();
			clientError.setCode(status != null ? status.name() : ErrorCode.UNKNOWN_EXCEPTION.toString());
			clientError.setMessage(status != null ? status.getReasonPhrase() : ErrorCode.UNKNOWN_EXCEPTION.getMessage());
			return formatResponse(clientError, code);
		} else {
			ClientError clientError = new ClientError();
			clientError.setCode(ErrorCode.UNKNOWN_EXCEPTION.toString());
			clientError.setMessage(ErrorCode.UNKNOWN_EXCEPTION.getMessage());
            clientError.setDetail(exception.getMessage());
			return formatResponse(clientError, ErrorCode.UNKNOWN_EXCEPTION.getStatus());
		}
	}

	private Response formatResponse(ClientError clientError, Integer status) {
		int responseStatus = status != null ? status : 500;

		List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();

		// Set the default type to the request type or JSON, if not provided
		MediaType type = headers.getMediaType() != null ? headers.getMediaType() : MediaType.APPLICATION_JSON_TYPE;
		for (MediaType mediaType : mediaTypes) {
			// Exception should not be returned in octet stream
			if (!mediaType.equals(MediaType.APPLICATION_OCTET_STREAM_TYPE) && !mediaType.equals(MediaType.WILDCARD_TYPE)) {
				// Change type to reference response type as specified by client
				type = mediaType;
				break;
			}
		}

		clientError.setStatus(responseStatus);
		return Response
			.status(responseStatus)
			.entity(clientError)
			.type(type)
			.build();
	}
    
}
