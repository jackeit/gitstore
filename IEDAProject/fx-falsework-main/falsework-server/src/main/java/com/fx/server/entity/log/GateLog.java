package com.fx.server.entity.log;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * gate_log
 * @author 
 */
@Data
public class GateLog implements Serializable {
    private Integer id;

    private String menu;

    private String opt;

    private String uri;

    private Date crtTime;

    private String crtUser;

    private String crtName;

    private String crtHost;

    private String body;

    private static final long serialVersionUID = 1L;
}