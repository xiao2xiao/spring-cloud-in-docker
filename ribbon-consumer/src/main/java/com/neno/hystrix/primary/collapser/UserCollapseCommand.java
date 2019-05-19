package com.neno.hystrix.primary.collapser;

import com.neno.hystrix.primary.command.UserBatchCommand;
import com.neno.model.User;
import com.neno.service.UserService;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: root
 * @Date: 2019/5/16 15:27
 */
public class UserCollapseCommand extends HystrixCollapser<List<User>, User, Long> {

    private UserService userService;
    private Long userId;

    public UserCollapseCommand(UserService userService, Long userId) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("UserCollapseCommand"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
        this.userId = userId;
        this.userService = userService;
    }

    @Override
    public Long getRequestArgument() {
        return userId;
    }

    /**
     * ereateCommand:该方法的 collapsedRequests参数中保存了延迟时间窗中收
     * 集到的所有获取单个User的请求。通过获取这些请求的参数来组织上面我们准备的
     * 批量请求命令UserBatchCommand实例。
     *
     * @param collapsedRequests
     * @return
     */
    @Override
    protected HystrixCommand<List<User>> createCommand(Collection<CollapsedRequest<User, Long>> collapsedRequests) {
        List<Long> ids = new ArrayList<>(collapsedRequests.size());
        ids.addAll(collapsedRequests.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList()));
        return new UserBatchCommand(userService, ids);
    }

    /**
     * mapResponseToRequests : 在批量请求命令UserBatchCommand实例被触发
     * 执行完 成 之 后 ， 该 方 法开始执行， 其中 batchResponse 参 数 保 存 了
     * createCommand中组织的批量请求命令的返回结果， 而 collapsedRequests
     * 参数则代表了每个被合并的请求。 在这里我们通过遍历批量结果batchResponse
     * 对象， 为 collapsedRequests中 每个合并前的单个请求设置返回结果， 以此完
     * 成批量结果到单个请求结果的转换。
     *
     * @param batchResponse
     * @param collapsedRequests
     */
    @Override
    protected void mapResponseToRequests(List<User> batchResponse, Collection<CollapsedRequest<User, Long>> collapsedRequests) {
        int index = 0;
        for (CollapsedRequest<User, Long> collapsedRequest : collapsedRequests) {
            User user = batchResponse.get(index++);
            collapsedRequest.setResponse(user);
        }
    }
}
