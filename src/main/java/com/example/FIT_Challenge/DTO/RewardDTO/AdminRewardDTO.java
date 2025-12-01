package com.example.FIT_Challenge.DTO.RewardDTO;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class AdminRewardDTO {
    private Long id ;
    private String name ;
    private String linkImage;
    private String description ;
    private Integer points ;
    private Integer total ;
    private String status ;
    private String externalPartner ;
    private Integer claimed ;
    private Date expireAt ;
}
