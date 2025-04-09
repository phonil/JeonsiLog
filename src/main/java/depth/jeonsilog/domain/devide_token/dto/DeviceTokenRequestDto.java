package depth.jeonsilog.domain.devide_token.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public class DeviceTokenRequestDto {

    @Data
    public static class DeviceTokenReq {

        @Schema(type = "string", example = "c8z22dyWSxqH_e7Gk..", description = "Device Token 입니다.")
        private String deviceToken;
    }
}
