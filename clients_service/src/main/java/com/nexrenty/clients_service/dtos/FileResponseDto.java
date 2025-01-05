package com.nexrenty.clients_service.dtos;


import java.time.Instant ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(description = "Response DTO for file-related operations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {


    @Schema(description = "The unique identifier of the file", example = "1")
    private long id ;

    @Schema(description = "The name of the file", example = "example.txt")
    private String filename ;

    @Schema(description = "The size of the file in bytes", example = "1024.0")
    private double size ;

    @Schema(description = "The content type of the file", example = "text/plain")
    private String contentType ;

    @Schema(description = "The bucket where the file is stored", example = "my-bucket")
    private String bucket ;


    @Schema(description = "The timestamp when the file was created", example = "2023-10-01T12:34:56Z")
    private Instant createAt ;
}