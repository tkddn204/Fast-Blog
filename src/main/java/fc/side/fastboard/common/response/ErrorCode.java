package fc.side.fastboard.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    FILE_NOT_FOUND("파일을 찾을 수 없습니다."),
    FILE_DUPLICATED("해당 파일이 이미 존재합니다."),
    FILE_DELETE_ERROR("파일 삭제에 실패했습니다."),
    FILE_SAVE_ERROR("파일 저장에 실패했습니다."),

    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),
    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),
    COMMON_ILLEGAL_STATUS("잘못된 상태값입니다.");

    private final String errorMsg;

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}
