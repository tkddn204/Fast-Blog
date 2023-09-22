package fc.side.fastboard.common.file.exception;

import fc.side.fastboard.common.exception.BaseException;
import fc.side.fastboard.common.response.ErrorCode;

public class FileNotFoundException extends BaseException {

  private final static ErrorCode errorCode = ErrorCode.FILE_NOT_FOUND;
  private final String message;

  public FileNotFoundException() {
    super(errorCode);
    this.message = errorCode.getErrorMsg();
  }

  public FileNotFoundException(String message) {
    super(message, errorCode);
    this.message = message;
  }
}
