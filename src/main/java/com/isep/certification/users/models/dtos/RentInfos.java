package com.isep.certification.users.models.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RentInfos {
    public Integer rentRequests;
    public Integer belongings;
    public Integer ongoingRents;
}
