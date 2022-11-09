package eu.dissco.core.translator.exception;

import org.springframework.dao.DataAccessException;

public class DisscoJsonMappingException extends DataAccessException {

  public DisscoJsonMappingException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
