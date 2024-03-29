package fc.side.fastboard.board.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

@Getter
public class PageNumber <T> {
    private final Page<T> page;
    private final int nowPage;
    private final int startPage;
    private final int endPage;

    private final static int PAGING_WIDTH = 3;

    public PageNumber(Page<T> page) {
        int nowPage = page.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage-PAGING_WIDTH, 1);
        int endPage = Math.min(nowPage+PAGING_WIDTH+1, page.getTotalPages());

        this.page = page;
        this.nowPage = nowPage;
        this.startPage = startPage;
        this.endPage = endPage;
    }
}
