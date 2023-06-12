package com.boardProject.board.mapper;

import com.boardProject.board.dto.PhotoDto;
import com.boardProject.board.entity.Photos;

public interface PhotoMapper {
    PhotoDto.Response photoToPhotoDtoResponse(Photos photo);
}
