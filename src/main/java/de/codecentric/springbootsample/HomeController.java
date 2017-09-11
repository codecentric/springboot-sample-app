/*
 * Copyright 2016 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.codecentric.springbootsample;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {

    private RecordRepository repository;
    private String valueVolume, valueDb;

    @Autowired
    public HomeController(RecordRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(ModelMap model) {
        model.addAttribute("env", System.getenv("ENVIRONMENT_VARIABLE"));
        model.addAttribute("configmap", System.getenv("CONFIG_MAP"));
        model.addAttribute("secret", System.getenv("SECRET"));
        try {
            valueVolume = readFile("/etc/config/httpd.conf", Charset.defaultCharset());
        } catch (IOException e) {
            valueVolume = "can't fetch file from volume";
        }
        model.addAttribute("volume", valueVolume);

        return "home";
    }

    String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
