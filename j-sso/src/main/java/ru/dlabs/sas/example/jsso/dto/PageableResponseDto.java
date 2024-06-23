package ru.dlabs.sas.example.jsso.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Getter
@NoArgsConstructor
public class PageableResponseDto<T> {

    private List<T> data;
    private Boolean moreDataExists;
    private Long total;

    public PageableResponseDto(List<T> data, Boolean moreDataExists, Long total) {
        this.data = data;
        this.moreDataExists = moreDataExists;
        this.total = total;
    }

    public static <T> PageableResponseDto<T> build(List<T> data, Pageable pageable, Long total) {
        boolean moreExists = false;

        if (!pageable.equals(Pageable.unpaged())) {
            moreExists = total > pageable.getOffset() + pageable.getPageSize();
        }

        return new PageableResponseDto<>(data, moreExists, total);
    }

    public static <T> PageableResponseDto<T> build(List<T> data, Boolean moreDataExists, Long total) {
        return new PageableResponseDto<>(data, moreDataExists, total);
    }
}
