package fc.side.fastboard.user.repository;

import fc.side.fastboard.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
