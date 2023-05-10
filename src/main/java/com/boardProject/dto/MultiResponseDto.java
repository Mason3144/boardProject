package com.boardProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class MultiResponseDto<T> {
    private List<T> data;
    private PageDto.Response pageInfo;
    public MultiResponseDto(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = PageDto.Response.builder()
                .page(page.getNumber()+1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
