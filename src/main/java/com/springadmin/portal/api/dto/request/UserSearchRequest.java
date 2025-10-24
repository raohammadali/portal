package com.springadmin.portal.api.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserSearchRequest {
    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 10;

    private String sortBy = "firstName";

    @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String direction = "asc";
}

