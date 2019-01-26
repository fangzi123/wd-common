package com.wdcloud.mq.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserJoinedCourseDto implements Serializable {
    private Long userId;
    private Long courseId;
    private Long sectionId;
    private Long roleId;

    private static final long serialVersionUID = -970725193464743191L;
}
