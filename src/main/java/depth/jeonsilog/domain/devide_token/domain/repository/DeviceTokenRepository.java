package depth.jeonsilog.domain.devide_token.domain.repository;

import depth.jeonsilog.domain.devide_token.domain.DeviceToken;
import depth.jeonsilog.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    Optional<DeviceToken> findByDeviceToken(String deviceToken);

    List<DeviceToken> findAllByUser(User receiver);
}
