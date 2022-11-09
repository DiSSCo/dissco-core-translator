package eu.dissco.core.translator.exception;

public class DisscoEfgParsingException extends Exception{

  public DisscoEfgParsingException(String message) {
    super(message);
  }

  public DisscoEfgParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}
