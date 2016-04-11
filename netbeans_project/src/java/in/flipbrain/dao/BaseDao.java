/**
 * Copyright 2015 Balwinder Sodhi
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.flipbrain.dao;

import in.flipbrain.dto.ClientInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author auser
 */
public abstract class BaseDao {

    protected enum Operation {

        Ins, Upd, Del
    }

    protected static SqlSessionFactory sqlSessionFactory;
    protected Logger log = Logger.getLogger(getClass().getName());
    protected ClientInfo clientInfo;

    public BaseDao() {
    }

    protected void init() throws IOException {
        String resource = "in/flipbrain/dao/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        /*
        Properties prop = new Properties();
        prop.put("db_url", "");
        prop.put("db_driver", "");
        prop.put("db_username", "");
        prop.put("db_password", "");
         */
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        log.info("Initialized the DAO");
    }

    private HashMap<String, String> getActivity() {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        String activity = st[4].getMethodName();
        HashMap<String, String> map = new HashMap<>();
        map.put("activity", activity);
        map.put("appUser", clientInfo.getAppUser());
        map.put("clientIp", clientInfo.getClientIp());
        return map;
    }

    private void logActivity(final SqlSession session) {
        session.insert("logActivity", getActivity());
    }

    protected <T> int saveEntity(String stmtId, T dto, Operation op) {
        int rows = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            logActivity(session);
            switch (op) {
                case Del:
                    rows = session.delete(stmtId, dto);
                    break;
                case Ins:
                    rows = session.insert(stmtId, dto);
                    break;
                case Upd:
                    rows = session.update(stmtId, dto);
                    break;
            }
            session.commit();
        } finally {
            session.close();
        }
        return rows;
    }

    protected <P, R> List<R> getEntityList(String stmtId, P dto) {
        SqlSession session = sqlSessionFactory.openSession();
        List<R> result;
        try {
            logActivity(session);
            result = session.selectList(stmtId, dto);
            session.commit();
        } finally {
            session.close();
        }
        return result;
    }

    protected <P, R> R getEntity(String stmtId, P dto) {
        SqlSession session = sqlSessionFactory.openSession();
        R result;
        try {
            logActivity(session);
            result = session.selectOne(stmtId, dto);
            session.commit();
        } finally {
            session.close();
        }
        return result;
    }

}
