package fc.side.fastboard.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardErrorCode {

    BOARD_NO_EXIST("해당 id의 게시글이 없습니다."),
    CANNOT_SAVE_BOARD("게시글을 생성하는 도중 에러가 발생했습니다."),
    CANNOT_DELETE_BOARD("게시글을 삭제하는 도중 에러가 발생했습니다."),
    ;

    private final String message;

}
