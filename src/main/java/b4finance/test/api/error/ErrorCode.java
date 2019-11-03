/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b4finance.test.api.error;

/**
 *
 * @author g578689
 */
public enum ErrorCode {
    
    UNKNOWN_EXCEPTION(500, "Unknown exception."),
    INVALID_REQUEST(400, "Invalid input request"),
    ENTITY_NOT_FOUND(400, "Could not find the entity"),
    ENTITY_ALREADY_EXISTS(500, "Entity already exists"),
    OWNERSHIP_NOT_FOUND(400, "Could not find the ownship between two entities"),
    INVALID_ENTITY_TYPE(400, "Invalid entity type requested");
    
    private String message;
    private int status;

    ErrorCode(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
    
}
