package com.rustdesk.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Change Password Request DTO
 *
 * @author RustDesk
 * @version 2.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Change Password Request")
public class ChangePasswordRequest {

    @NotBlank(message = "Old password cannot be blank")
    @Schema(description = "Old Password", example = "oldpassword123", required = true)
    private String oldPassword;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, message = "New password must be at least 6 characters")
    @Schema(description = "New Password", example = "newpassword123", required = true)
    private String newPassword;
}
