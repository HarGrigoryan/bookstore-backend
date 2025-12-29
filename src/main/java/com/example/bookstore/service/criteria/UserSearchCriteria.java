package com.example.bookstore.service.criteria;

import com.example.bookstore.enums.PermissionName;
import com.example.bookstore.enums.RoleName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
public class UserSearchCriteria extends SearchCriteria {

    private String firstname;
    private String lastname;
    private String email;
    private Boolean enabled;
    private Instant createdAt = LocalDateTime.of(1000, 1, 1, 0, 0).toInstant(ZoneOffset.MIN); //LocalDateTime.of(1000, 1, 1, 0, 0);
    private Instant updatedAt = LocalDateTime.of(1000, 1, 1, 0, 0).toInstant(ZoneOffset.MIN);
    private RoleName roleName;
    private PermissionName permissionName;
    private String sortBy = "lastname";

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();
        if(getSortDirection().equals(SortDirection.ASC))
            return pageRequest.withSort(
                    Sort.by(sortBy).ascending()
            );
        return pageRequest.withSort(
                Sort.by(sortBy).descending());
    }


}
