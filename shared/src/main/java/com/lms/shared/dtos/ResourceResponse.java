package com.lms.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceResponse {

    private long id;
    private String title;
    private String fileType;
    private String fileName;
    private long fileSize;
    private byte[] fileData;
}
