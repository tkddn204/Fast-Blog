package fc.side.fastboard.board.service;

import fc.side.fastboard.board.dto.BoardDTO;
import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.request.CreateBoardRequest;
import fc.side.fastboard.board.dto.request.UpdateBoardRequest;

import java.util.List;

public interface BoardServiceImpl {
  List<BoardDTO> getBoardList(int page);
  BoardDetailDTO getBoardById(int id);

  int createBoard(CreateBoardRequest request);

  int updateBoard(UpdateBoardRequest request);

  int deleteBoard(int id);
}
