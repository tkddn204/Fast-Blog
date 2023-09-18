package fc.side.fastboard.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardException extends RuntimeException {

    private BoardErrorCode errorCode;

}
