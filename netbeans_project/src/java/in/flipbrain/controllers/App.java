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
package in.flipbrain.controllers;

import in.flipbrain.AuthContextImpl;
import in.flipbrain.Constants;
import in.flipbrain.Utils;
import in.flipbrain.dao.MyBatisDao;
import in.flipbrain.dto.UserDto;
import java.io.IOException;
import org.javamvc.core.annotations.Action;
import org.javamvc.core.annotations.Authorize;

/**
 *
 * @author auser
 */
public class App extends BaseController {

    @Action
    public void login() throws IOException {
        try {
            if (isJsonRequest()) {
                UserDto user = getJsonRequestAsObject(UserDto.class);
                MyBatisDao dao = MyBatisDao.getInstance(getClientInfo());
                UserDto u2 = dao.getUserByEmail(user.email);
                String hashed = Utils.hashPassword(user.password);
                if (u2 != null && u2.hashedPassword.equals(hashed)) {
                    u2.password = null;
                    u2.hashedPassword = null;
                    setSessionAttribute(Constants.SK_USER, u2);
                    setSessionAttribute(Constants.SK_AUTH_CTX, new AuthContextImpl(u2));
                    sendJsonResponse(Status.OK, u2);
                } else {
                    sendJsonResponse(Status.ERROR, "Authentication failed.");
                }

            } else {
                sendJsonResponse(Status.ERROR, "Expected request in JSON format.");
            }

        } catch (Exception e) {
            handleActionError(e);
        }
    }

    @Action
    public void logout() throws IOException {
        invalidateSession();
        sendJsonResponse(Status.OK, "Logged out");
    }

    @Action
    public void register() throws IOException {
        try {
            if (isJsonRequest()) {
                UserDto user = getJsonRequestAsObject(UserDto.class);
                MyBatisDao dao = MyBatisDao.getInstance(getClientInfo());
                dao.saveUser(user);
                user.password = null;
                user.hashedPassword = null;

                setSessionAttribute(Constants.SK_USER, user);
                setSessionAttribute(Constants.SK_AUTH_CTX, new AuthContextImpl(user));

                sendJsonResponse(Status.OK, user);
            } else {
                sendJsonResponse(Status.ERROR, "Expected request in JSON format.");
            }
        } catch (Exception e) {
            handleActionError(e);
        }
    }

    @Authorize
    @Action
    public void saveUser() throws IOException {
        try {

            if (isJsonRequest()) {
                UserDto user = getJsonRequestAsObject(UserDto.class);
                UserDto inSession = getSessionAttribute(Constants.SK_USER);
                if (inSession.userId != user.userId) {
                    sendJsonResponse(Status.ERROR, "Illegal attempt to change user details! Will be reported.");
                } else {
                    MyBatisDao dao = MyBatisDao.getInstance(getClientInfo());
                    dao.saveUser(user);
                    user.password = null;
                    user.hashedPassword = null;
                    setSessionAttribute(Constants.SK_USER, user);
                    sendJsonResponse(Status.OK, user);
                }
            } else {
                sendJsonResponse(Status.ERROR, "Expected request in JSON format.");
            }
        } catch (Exception e) {
            handleActionError(e);
        }
    }

    @Action
    public void getSessionDetails() throws IOException {
        UserDto u = getSessionAttribute(Constants.SK_USER);
        sendJsonResponse(Status.OK, u);
    }

    @Action
    public void isLoggedIn() throws IOException {
        UserDto u = getSessionAttribute(Constants.SK_USER);
        sendJsonResponse(Status.OK, u != null);
    }

    @Action
    @Authorize(roles = {"admin", "editor"})
    public void rbacTest() throws IOException {
        sendJsonResponse(Status.OK, "This is a test of RBAC.");
    }
}
