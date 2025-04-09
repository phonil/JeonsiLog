package depth.jeonsilog.domain.devide_token.domain;

import depth.jeonsilog.domain.common.BaseEntity;
import depth.jeonsilog.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class DeviceToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_token", nullable = false, unique = true)
    private String deviceToken;

    public void updateUser(User user) {
        this.user = user;
    }

    @Builder
    public DeviceToken(User user, String deviceToken) {
        this.user = user;
        this.deviceToken = deviceToken;
    }
}
