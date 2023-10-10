package fc.side.fastboard.user.entity;

import fc.side.fastboard.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "EMAIL_UNIQUE",
        columnNames = "EMAIL"
)})
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column
    private String fileName;

    @Builder
    public User(Long id, String email, String password, String username, String fileName) {
        this.id  = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.fileName = fileName;
    }

}
