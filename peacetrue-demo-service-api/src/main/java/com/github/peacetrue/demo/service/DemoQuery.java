package com.github.peacetrue.demo.service;

import com.github.peacetrue.core.Range;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xiayx
 */
@Data
@NoArgsConstructor
public class DemoQuery implements Serializable {

    public static final DemoQuery DEFAULT = new DemoQuery();

    private static final long serialVersionUID = 0L;

    private String code;
    private String name;
    private Range.Date createdTime;


}
