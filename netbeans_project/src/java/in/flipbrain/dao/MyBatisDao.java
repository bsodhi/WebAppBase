/*
Copyright 2015 Balwinder Sodhi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package in.flipbrain.dao;

import com.mysql.jdbc.StringUtils;
import in.flipbrain.Utils;
import in.flipbrain.dto.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author Balwinder Sodhi
 */
public class MyBatisDao extends BaseDao {


    private static class MyBatisDaoHolder {
        private static final MyBatisDao INSTANCE = new MyBatisDao();
    }

    public static MyBatisDao getInstance(ClientInfo clientInfo) {
        MyBatisDaoHolder.INSTANCE.clientInfo = clientInfo;
        return MyBatisDaoHolder.INSTANCE;
    }

    private MyBatisDao() {
        try {
            init();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Failed to initialize DAO.", ex);
        }
    }

    //////////////// DAO methods /////////////////////

    public void saveUser(UserDto dto) {
        // Password (if present) will be saved after hashing
        if (!StringUtils.isEmptyOrWhitespaceOnly(dto.password)) {
            dto.hashedPassword = Utils.hashPassword(dto.password);
        }
        if (dto.userId > 0) {
            saveEntity("updateUser", dto, Operation.Upd);
        } else {
            saveEntity("insertUser", dto, Operation.Ins);
        }
    }

    public UserDto getUserByLogin(String login) {
        return getEntity("getUserByLogin", login);
    }

    public UserDto getUserByEmail(String email) {
        return getEntity("getUserByEmail", email);
    }

    public UserDto getUserById(long userId) {
        return getEntity("getUserById", userId);
    }

    public void recordLoginAttempt(HashMap<String, Object> param) {
        saveEntity("recordLoginAttempt", param, Operation.Upd);
    }

}
