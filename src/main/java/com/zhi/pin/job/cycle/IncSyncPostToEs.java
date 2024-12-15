package com.zhi.pin.job.cycle;

//import com.cetide.oj.esdao.PostEsDao;

import com.zhi.pin.mapper.PostMapper;
import com.zhi.pin.model.dto.post.PostEsDTO;
import com.zhi.pin.model.entity.Post;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 增量同步帖子到 es
 *
 */
// todo 取消注释开启任务
@Component
public class IncSyncPostToEs {

    @Resource
    private PostMapper postMapper;

//    @Resource
//    private PostEsDao postEsDao;

    /**
     * 每分钟执行一次
     */
//    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        // 查询近 5 分钟内的数据
        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 5 * 60 * 1000L);
        List<Post> postList = postMapper.listPostWithDelete(fiveMinutesAgoDate);
        if (CollectionUtils.isEmpty(postList)) {
            System.out.println("no inc post");
            return;
        }
        List<PostEsDTO> postEsDTOList = postList.stream()
                .map(PostEsDTO::objToDto)
                .collect(Collectors.toList());
        final int pageSize = 500;
        int total = postEsDTOList.size();
        System.out.println("IncSyncPostToEs start,total: "+total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            System.out.println("sync from "+i+" to "+end);
//            postEsDao.saveAll(postEsDTOList.subList(i, end));
        }
        System.out.println("IncSyncPostToEs end Total:"+total);
    }
}
