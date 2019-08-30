package com.ysma.ppt.intf.entity;

import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 序列号demo
 */
@Data
public class Demo implements Serializable {

    //瞬态变量
    private transient int data;

    private static final long serialVersionUID = 1587566894450763796L;

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(data);
    }

    /**
     * private 方法是只有实例自己可见的  它是如何被执行的呢？
     * ObjectInputStream->readObject方法
     * ->readObject0方法->TC_CLASSDESC位置
     * ->readClassDesc方法->TC_CLASSDESC位置
     * ->readNonProxyDesc方法->initNonProxy方法
     * ObjectStream ->ObjectStreamClass方法
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }

}
