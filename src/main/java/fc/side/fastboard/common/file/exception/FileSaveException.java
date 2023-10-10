package fc.side.fastboard.common.file.exception;

import fc.side.fastboard.common.exception.BaseException;
import fc.side.fastboard.common.response.ErrorCode;

public class FileSaveException extends BaseException {

  private final static ErrorCode errorCode = ErrorCode.FILE_SAVE_ERROR;
  private final String message;

  public FileSaveException() {
    super(errorCode);
    this.message = errorCode.getErrorMsg();
  }

  public FileSaveException(String message) {
    super(message, errorCode);
    this.message = errorCode.getErrorMsg() + " : " + message;
  }
}
