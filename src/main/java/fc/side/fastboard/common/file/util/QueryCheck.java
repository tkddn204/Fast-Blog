package fc.side.fastboard.common.file.util;

import fc.side.fastboard.common.exception.InvalidParamException;

public class QueryCheck {

  public enum QueryType {
    UUID_TYPE,
    ORIGIN_FILE_NAME_TYPE
  }

  public static QueryType check(String query) {
    if (query.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
      return QueryType.UUID_TYPE;
    } else if (query.contains(".")) {
      return QueryType.ORIGIN_FILE_NAME_TYPE;
    }

    throw new InvalidParamException();
  }
}
