package com.dc.rong.web;

import com.dc.rong.bean.UserTb;
import com.dc.rong.service.UserService;
import com.dc.rong.utils.ExcelUtils;
import com.dc.rong.utils.RongBaseSubscriber;
import com.dc.rong.utils.UniversalViewResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.View;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Api(tags = "接口类")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/tt/aa")
public class UserController {

    @NonNull
    private UserService userService;

    private Scheduler scheduler = Schedulers.newParallel("handle-user-io");


    @PutMapping("/addUser/{id}")
    @ApiOperation(value = "addUser", notes = "添加用户", httpMethod = "PUT")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id",value = "用来作为用户名称", required = true, paramType = "int"),
//    })
    public void addUser(@ApiParam(name = "id", value = "用户作为用户名称", required = true) @PathVariable("id") String userId) {
        log.info("userId is {}",userId);
        String format = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        String password = DigestUtils.md5DigestAsHex(format.getBytes());
        UserTb buildUserTb = UserTb.builder()
                .name(userId)
                .password(password)
                .userState(UserTb.UserState.NEW)
                .expireTime(LocalDate.now().plus(Period.ofDays(7))).build();
        userService.addUser(buildUserTb);
    }


    @ApiOperation(value = "findAllUser", notes = "查询所有用户", httpMethod = "GET")
    @GetMapping("/user/all")
    public DeferredResult<List<UserTb>> findAllUser(){
        DeferredResult<List<UserTb>> deferredResult = new DeferredResult<>();
        userService.findUserByName()
                .collectList()
                .delayElement(Duration.ofSeconds(2))
                .doOnNext(deferredResult::setResult)
                .subscribeOn(scheduler)
                .subscribe(new RongBaseSubscriber<>());
        log.info("## DeferredResult async execute()");
        return deferredResult;
    }


    @GetMapping("/user/excel")
    @ApiOperation(value = "generateExcel",notes = "生成excel文件",httpMethod = "GET")
    public void generateExcel(){
        userService.findAllUser()
                .buffer()
                .next()
                .doOnNext(dtos -> {
                    ExcelUtils.writeExcel(Paths.get("用户.xlsx"),dtos);
                })
                .subscribe(new RongBaseSubscriber<>());
    }


    @GetMapping("/hello1")
    public View redirectHello() throws Exception {
       return UniversalViewResolver.forwardView("/hello",this.getClass());
    }


    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
