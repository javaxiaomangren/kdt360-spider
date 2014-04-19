package com.amap.task.utils;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.UnknownHostException;

/**
 * mongo db utils
 * Created by yang.hua on 14-4-2.
 */
public class MongoUtils {

    private static PropertiesConfiguration conf = Utils.getConf("conf/mongo.properties");
    private static MongoClient client = getMongoClient();

    public static DB getMongoDb(String name) {
        if (client == null) {
            client = getMongoClient();
        }
        if (name == null) {
            name = conf.getString("db");
        }
        return client.getDB(name);
    }

    public static DB getMongoDb() {
        return getMongoDb(null);
    }

    private static MongoClient getMongoClient() {
        try {
            return new MongoClient(conf.getString("host"), conf.getInt("port"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
