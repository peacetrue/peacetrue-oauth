package com.github.peacetrue.demo.service;

import com.github.peacetrue.spring.util.BeanUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xiayx
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestServiceDemoAutoConfiguration.class)
@Transactional
public class DemoServiceImplTest {

    @Autowired
    private DemoService demoService;

    public static final DemoAdd<Long> DEMO_ADD = new DemoAdd<>();

    static {
        DEMO_ADD.setCode("2");
        DEMO_ADD.setName("2");
        DEMO_ADD.setOperatorId(1L);
    }

    @Test
    public void add() {
        DemoVO<Long, Long> vo = demoService.add(DEMO_ADD);
        Assert.assertEquals(vo, demoService.<Long, Long>get(new DemoGet<>(vo.getId())));
    }

    @Test
    public void query() {
        Page<DemoVO<Long, Long>> vos = demoService.query(new DemoQuery(), new PageRequest(0, 1));
        Assert.assertEquals(vos.getTotalElements(), 1);
    }

    @Test
    public void get() {
        DemoVO<Long, Long> vo = demoService.get(new DemoGet<>(1L));
        Assert.assertNotNull(vo);
    }

    @Test
    public void modify() {
        DemoVO<Long, Long> vo = demoService.get(new DemoGet<>(1L));
        DemoModify<Long, Long> modify = new DemoModify<>();
        BeanUtils.copyProperties(vo, modify);
        modify.setCode("2");
        int count = demoService.modify(modify);
        Assert.assertEquals(count, 1);
        vo = demoService.get(new DemoGet<>(1L));
        Assert.assertEquals(modify.getCode(), vo.getCode());
    }

    @Test
    public void delete() {
        int count = demoService.delete(new DemoDelete<>(new Long[]{1L}));
        Assert.assertEquals(count, 1);
    }
}