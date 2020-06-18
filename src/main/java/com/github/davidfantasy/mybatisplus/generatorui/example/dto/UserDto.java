package com.github.davidfantasy.mybatisplus.generatorui.example.dto;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * 该代码由mybatis-plus-generator-ui自动生成
 *
 * @author david
 * @since 2020-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserDto {

    private Integer id;

    private Integer parentId;

    private String company;

    private String industry;

    private String address;

    private String recycleAddress;

    private Integer level;

    private String productLine;

    private Double lat;

    private Double lng;

    private Integer status;

    private String contact;

    private String mailAddress;

    private String city;

    private String remark;

    private String companyShort;

    private String boxType;

    private String invoiceInfo;

    private String creator;

    private Date createTime;

    private String generalRemark;

    private String province;

    private Integer isReconciliation;

    private String overdueMode;

    private Integer chargeType;

}
