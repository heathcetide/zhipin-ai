package com.zhi.pin.controller;

import cn.hutool.core.io.FileUtil;
import com.zhi.pin.common.BaseResponse;
import com.zhi.pin.common.ErrorCode;
import com.zhi.pin.common.ResultUtils;
import com.zhi.pin.exception.BusinessException;
import com.zhi.pin.manager.CosManager;
import com.zhi.pin.model.enums.FileUploadBizEnum;
import com.zhi.pin.service.UserService;
import com.zhi.pin.utils.CosUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * 文件接口
 *
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    /**
     * 文件上传
     *
     * @return
     */


    @PostMapping("/upload")
    public BaseResponse<String> uploadPic(@RequestPart MultipartFile file) throws Exception {
        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return ResultUtils.error(ErrorCode.valueOf("Invalid file name"));
            }
            String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
            // 保存临时文件
            File tempFile = new File("/tmp/" + filename);
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }
            // 上传文件
            String publicUrl = CosUtils.uploadFile(tempFile, filename);
            // 删除临时文件
            tempFile.delete();
            return ResultUtils.success(publicUrl);
        } catch (IOException e) {
            return ResultUtils.error(ErrorCode.valueOf("Failed to upload file: " + e.getMessage()));
        }
    }
//    @PostMapping("/upload")
//    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
//            UploadFileRequest uploadFileRequest, HttpServletRequest request) {
//        String biz = uploadFileRequest.getBiz();
//        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
//        if (fileUploadBizEnum == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        validFile(multipartFile, fileUploadBizEnum);
//        User loginUser = userService.getLoginUser(request);
//        // 文件目录：根据业务、用户来划分
//        String uuid = RandomStringUtils.randomAlphanumeric(8);
//        String filename = uuid + "-" + multipartFile.getOriginalFilename();
//        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
//        File file = null;
//        try {
//            // 上传文件
//            file = File.createTempFile(filepath, null);
//            multipartFile.transferTo(file);
//            cosManager.putObject(filepath, file);
//            // 返回可访问地址
//            return ResultUtils.success(FileConstant.COS_HOST + filepath);
//        } catch (Exception e) {
////            log.error("file upload error, filepath = " + filepath, e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
//        } finally {
//            if (file != null) {
//                // 删除临时文件
//                boolean delete = file.delete();
//                if (!delete) {
////                    log.error("file delete error, filepath = {}", filepath);
//                }
//            }
//        }
//    }

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }
}
