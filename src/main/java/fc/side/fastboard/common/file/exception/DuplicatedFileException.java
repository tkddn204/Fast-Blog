package fc.side.fastboard.common.file.exception;

import fc.side.fastboard.common.exception.BaseException;
import fc.side.fastboard.common.response.ErrorCode;

public class DuplicatedFileException extends BaseException {

  private final static ErrorCode errorCode = ErrorCode.FILE_DUPLICATED;
  private final String message;

  public DuplicatedFileException() {
    super(errorCode);
    this.message = errorCode.getErrorMsg();
  }

  public DuplicatedFileException(String message) {
    super(message, errorCode);
    this.message = message;
  }
}
