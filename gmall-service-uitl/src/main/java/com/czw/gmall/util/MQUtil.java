package com.czw.gmall.util;

import com.czw.gmall.mq.ActiveMQUtil;
import org.apache.activemq.command.ActiveMQMapMessage;

import javax.jms.*;

public class MQUtil {
    //队列模式
    public static void createMyQueue(ActiveMQUtil activeMQUtil,String queueName,String params){
        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);//开启事务

            Queue queue = session.createQueue(queueName);//创建队列
            MessageProducer producer = session.createProducer(queue);

            //存入消息数据给消费者
            ActiveMQMapMessage activeMQMapMessage = new ActiveMQMapMessage();
            activeMQMapMessage.setString("out_trade_no",params);
            //发送消息
            producer.send(activeMQMapMessage);
            //提交事务
            session.commit();
        } catch (JMSException e) {
            try {
                session.rollback();//回滚
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            try {
                session.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
