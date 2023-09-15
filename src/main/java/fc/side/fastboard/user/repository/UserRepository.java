package fc.side.fastboard.user.repository;

import fc.side.fastboard.user.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
