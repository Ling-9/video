package com.fgsqw.beans.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="page", description="")
public class Page {

    @ApiModelProperty(value = "页数")
    private Integer page;

    @ApiModelProperty(value = "显示条数")
    private Integer limit;
}
