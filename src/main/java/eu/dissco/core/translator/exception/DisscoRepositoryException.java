package eu.dissco.core.translator.exception;

public class DisscoRepositoryException extends Exception {

  public DisscoRepositoryException(String message) {
    super(message);
  }

  public DisscoRepositoryException(String message, Throwable cause) {
    super(message, cause);
  }
}
