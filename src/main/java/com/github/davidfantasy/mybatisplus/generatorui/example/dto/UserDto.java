package com.github.davidfantasy.mybatisplus.generatorui.example.dto;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * 该代码由mybatis-plus-generator-ui自动生成
 *
 * @author david
 * @since 2020-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserDto {

    private Integer id;

    private String account;

    private String password;

    private String name;

    private String department;

    private Integer status;

    private String phone;

    private String function;

    private String job;

    private String email;

    private String creator;

    private Date createdtime;

    private String inheritance;

    private String updateBy;

    private Date updateTime;




}
