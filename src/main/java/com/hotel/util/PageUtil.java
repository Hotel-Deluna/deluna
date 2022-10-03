package com.hotel.util;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PageUtil {

    /**
     *  페이징 처리 유틸 - 리스트를 받아서 페이징 처리 후 다시 리턴
     * @param page : 해당 페이지
     * @param pageCnt : 페이지당 갯수
     * @param totalCnt : 총 데이터 갯수
     * @param paramList : 페이징 처리할 리스트
     * @param <T>
     * @return
     */

    public static <T> List<T> paginationList(int page, int pageCnt, int totalCnt, List<T> paramList){

        PagedListHolder pagedListHolder = new PagedListHolder(paramList);
        pagedListHolder.setPage(page-1); // 페이지가 0부터 시작이라 1빼줌
        pagedListHolder.setPageSize(pageCnt);
        pagedListHolder.setMaxLinkedPages(totalCnt);

        int lastPage = (totalCnt % pageCnt) == 0 ? totalCnt/pageCnt : (totalCnt/pageCnt)+1;

        // 마지막 페이지의 다음 페이지 요청 들어오면 빈 배열 리턴
        if(lastPage < page){
            return new ArrayList<>();
        }

        return pagedListHolder.getPageList();
    }
}
