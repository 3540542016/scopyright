package lltw.scopyright.controller;


import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.form.LoginForm;
import lltw.scopyright.form.RegisterFrom;
import lltw.scopyright.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/login")
    public ResultVO login(@RequestBody LoginForm loginForm) throws NoSuchAlgorithmException {
        ResultVO resultVO = usersService.login(loginForm);
        return resultVO;
    }

    @PostMapping("/register")
    public ResultVO register(@RequestBody RegisterFrom registerFrom) throws NoSuchAlgorithmException {
        ResultVO resultVO = usersService.register(registerFrom);
        return resultVO;
    }

    @PostMapping("/test")
    public ResultVO test() {
        return ResultVO.success("sakura");
    }
}

