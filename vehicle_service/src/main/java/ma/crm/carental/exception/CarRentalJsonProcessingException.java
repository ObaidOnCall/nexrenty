package ma.crm.carental.exception;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonLocation;

public class CarRentalJsonProcessingException extends JacksonException{

    public CarRentalJsonProcessingException(String msg) {
        super(msg);
    }

    public CarRentalJsonProcessingException(String msg , Throwable th) {
        super(msg , th);
    }

    @Override
    public JsonLocation getLocation() {
        throw new UnsupportedOperationException("Unimplemented method 'getLocation'");
    }

    @Override
    public String getOriginalMessage() {
        throw new UnsupportedOperationException("Unimplemented method 'getOriginalMessage'");
    }

    @Override
    public Object getProcessor() {
        throw new UnsupportedOperationException("Unimplemented method 'getProcessor'");
    }
    
}
