package com.damuzee.boot.spec.tests;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdjustBillModel implements Serializable {

    private Long id;

    /**
     * 模拟租户字段
     */
    private Long tenantId;

    private Long venderId;

    private String venderName;

    private Long localeId;

    private String localeName;

    private String billNo;

    private Long creatorId;

    private Long modifierId;
    private LocalDateTime modified;
    private LocalDateTime created;

}
