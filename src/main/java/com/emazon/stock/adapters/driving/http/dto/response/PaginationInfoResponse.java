package com.emazon.stock.adapters.driving.http.dto.response;

import com.emazon.stock.domain.model.PaginationInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationInfoResponse<T> {

    private PaginationInfo<T> paginationInfo;

}
