package com.fx.server.job;

import com.fx.server.entity.SysJob;
import com.fx.server.util.SpringUtils;
import com.fx.server.websocket.WebSocketMessageHandler;
import org.quartz.JobExecutionContext;
import org.springframework.web.socket.TextMessage;

/**
 * @description:
 * @className: TestJob
 * @author: liwen
 * @date: 2020/12/24 16:23
 */
public class TestJob extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
        System.err.println("aafdsafdsa");
        // 写入数据库当中
        SpringUtils.getBean(WebSocketMessageHandler.class).sendMessageToUsers(new TextMessage("999999999999999999999999999999"));
    }
}
