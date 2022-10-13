package com.fgsqw;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fgsqw.beans.exception.MyRuntimeException;
import com.fgsqw.beans.result.ResultCodeEnum;
import com.google.common.collect.Sets;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Set;

@Component
public class MinioUtil {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.imageBucketName}")
    private String imageBucket;
    @Value("${minio.fileBucketName}")
    private String fileBucket;
    @Value("${minio.videoBucketName}")
    private String videoBucket;

    /**
     * 允许的图片类型
     */
    private static final Set<String> imageType = Sets.newHashSet(
            ".jpg", ".JPG", ".png", ".PNG", ".pdf", ".PDF", ".jpeg", ".JPEG", ".gif", ".GIF"
    );
    /**
     * 允许的视频类型
     */
    private static final Set<String> videoType = Sets.newHashSet(
            ".mp4", ".mp3"
    );

    /**
     * 上传文件
     * @param file
     * @return
     * @throws IllegalArgumentException
     */
    public String upload(MultipartFile file) throws Exception {
        if(ObjectUtil.isEmpty(file)){
            throw new MyRuntimeException(ResultCodeEnum.ISNULL);
        }
        String originalFilename = file.getOriginalFilename();
        if(StrUtil.isEmpty(originalFilename)){
            throw new MyRuntimeException("获取文件参数失败!");
        }
        String remoteFileName = null;
        try {
            remoteFileName = remoteFileName(originalFilename);
            checkBucket();
            // 使用putObject上传一个文件到存储桶中。
            PutObjectOptions options = new PutObjectOptions(file.getSize(), -1);
            minioClient.putObject(bucket(file.getOriginalFilename()),remoteFileName,file.getInputStream(),options);
        } catch (Exception e) {
            throw new MyRuntimeException("文件上传失败");
        }
//        return Base64.encode(remoteFileName);
        return remoteFileName;
    }

    public void preview(String fileName) throws IllegalArgumentException {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
            response.setContentType("image/" + fileName.substring(fileName.indexOf(".") + 1));

            checkBucket();
            checkImage(fileName);

            @Cleanup
            InputStream inputStream = minioClient.getObject(imageBucket, fileName);
            @Cleanup
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
        } catch (Exception e) {
            throw new IllegalArgumentException("文件获取失败", e);
        }
    }

    private void checkImage(String fileName) throws IllegalArgumentException {
        int i = fileName.lastIndexOf(".");
        Assert.isTrue(i > 0, "图片格式不对，不支持预览");
        String fileType = fileName.substring(i);
        Assert.isTrue(imageType.contains(fileType), "非图片格式，不支持预览");
    }

    /**
     * 根据文件名选择桶
     * 图片类型放在图片桶，其余放在文件桶
     *
     * @param fileName 文件名
     * @return
     */
    private String bucket(String fileName) {
        int i = fileName.lastIndexOf(".");

        if (i < 0) {
            return fileBucket;
        }

        String fileType = fileName.substring(i);
        return imageType.contains(fileType) ? imageBucket : videoType.contains(fileType) ? videoBucket : fileBucket;
    }

    /**
     * 文件名称
     * 图片使用uuid重命名
     * 其他文件保持原名
     *
     * @param fileName
     * @return
     */
    private String remoteFileName(String fileName) {
        int i = fileName.lastIndexOf(".");

        if (i < 0) {
            return fileName;
        }

        String fileType = fileName.substring(i);
        if (imageType.contains(fileType)) {
//            return IdUtil.getSnowflakeNextId() + fileType;
            return IdUtil.simpleUUID() + fileType;
        }
        return fileName;
    }

    private void checkBucket() {
        try {
            if (!minioClient.bucketExists(imageBucket)) {
                minioClient.makeBucket(imageBucket);
            }
            if (!minioClient.bucketExists(fileBucket)) {
                minioClient.makeBucket(fileBucket);
            }
            if (!minioClient.bucketExists(videoBucket)) {
                minioClient.makeBucket(videoBucket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件名返回预览链接
     */
    public String pictureUrl(String remoteFileName) throws Exception{
       return minioClient.presignedGetObject(imageBucket,remoteFileName,60 * 60);
    }


}
