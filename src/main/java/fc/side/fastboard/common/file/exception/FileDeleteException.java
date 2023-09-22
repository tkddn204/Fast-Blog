package fc.side.fastboard.common.file.exception;

import fc.side.fastboard.common.exception.BaseException;
import fc.side.fastboard.common.response.ErrorCode;

public class FileDeleteException extends BaseException {

  private final static ErrorCode errorCode = ErrorCode.FILE_DELETE_ERROR;
  private final String message;

  public FileDeleteException() {
    super(errorCode);
    this.message = errorCode.getErrorMsg();
  }

  public FileDeleteException(String message) {
    super(message, errorCode);
    this.message = errorCode.getErrorMsg() + " : " + message;
  }
}
