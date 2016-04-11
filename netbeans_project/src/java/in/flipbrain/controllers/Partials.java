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

import java.io.IOException;

import org.javamvc.core.Controller;
import org.javamvc.core.annotations.Action;

/**
 * Purpose of this controller is to serve partials i.e. the HTML view
 * templates/markup only.
 *
 * @author Balwinder Sodhi
 */
public class Partials extends Controller {

    @Action
    public void home() throws IOException {
        View();
    }

    @Action
    public void login() throws IOException {
        View();
    }

    @Action
    public void register() throws IOException {
        View();
    }

    @Action
    public void userDetails() throws IOException {
        View();
    }
}
