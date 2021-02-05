package pw.react.backend.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
public class PagedResponse<T> {
    private int pageCount;
    private int pageSize;
    private int pageNumber;
    private T data;

    public PagedResponse(T data, int page, int size, int pageCount){
        this.data = data;
        this.pageNumber = page;
        this.pageSize = size;
        this.pageCount = pageCount;
    }
}
