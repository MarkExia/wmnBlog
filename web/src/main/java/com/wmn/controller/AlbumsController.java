package com.wmn.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wmn.VO.AlbumsVO;
import com.wmn.entity.Albums;
import com.wmn.entity.AlbumsClasses;
import com.wmn.mapper.AlbumsClassesMapper;
import com.wmn.mapper.AlbumsMapper;
import com.wmn.utils.JWTToken;
import com.wmn.utils.JWTVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wmn
 * @since 2020-03-13
 */
@Controller
@RequestMapping("/albums")
@ResponseBody
public class AlbumsController {
    private Map<String, Object> map;

    @Autowired
    AlbumsMapper albumsMapper;

    @Autowired
    AlbumsClassesMapper albumsClassesMapper;

    //String filepath = "/data/zhbweb/image/upload/";
    String filepath = "C:\\Users\\89243\\Desktop\\testimg\\";
    //上传图片
    @PostMapping("/upImg")
    public String uploadImg( @RequestParam("file") MultipartFile file) {


            if (!file.isEmpty()) { //文件不是空文件
                try {
                    String fileName = file.getOriginalFilename().toString();//获取文件名
                    final SimpleDateFormat sDateFormate = new SimpleDateFormat("yyyyMMddHHmmss");  //设置时间格式
                    String nowTimeStr = sDateFormate.format(new Date()); // 当前时间
                    fileName = fileName.substring(0, fileName.indexOf(".")) + nowTimeStr + fileName.substring(fileName.lastIndexOf("."));
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(new File(filepath + fileName)));//保存图片到目录下,建立保存文件的输入流
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                    String filename = filepath + fileName;
                    Long fileSize = file.getSize();
                    System.out.println(file.getSize());
                    map = new HashMap<String, Object>();

                    map.put("code", 200);
                    map.put("msg", "上传图片成功");
                    map.put("url",filename);
                    return JSON.toJSONString(map);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    map.put("code", 500);
                    map.put("msg", "上传失败"+e.getMessage());
                    return JSON.toJSONString(map);
                    //return "上传失败," + e.getMessage();  //文件路径错误
                } catch (IOException e) {
                    e.printStackTrace();
                    map.put("code", 500);
                    map.put("msg", "上传失败"+e.getMessage());
                    return JSON.toJSONString(map);

                    //return "上传失败," + e.getMessage();  //文件IO错误
                }

                //return new Reponse(true, "上传头像成功", user);//返回用户信息
            } else {
                map.put("code", 500);
                map.put("msg", "上传失败，因为文件是空的");
                return JSON.toJSONString(map);
                //return new Reponse(false, "上传失败，因为文件是空的");
            }

    }
        @PostMapping("/delImg")
    public String deleteImg(@RequestHeader("Authorization") String authorization, @RequestParam("path") String path) {
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null || !"admin".equals(JWTToken.isAdmin(authorization))) {
            return JSON.toJSONString(erroToken);
        } else {
            boolean b = deleteServerFile(path);
            HashMap ob = new HashMap<String,String>();
            ob.put("path", path);
            int isDelete = albumsMapper.deleteByMap(ob);

            map = new HashMap<String, Object>();
            if (isDelete != 0) {
                map.put("code", 200);
                map.put("msg", "删除成功");
                return JSON.toJSONString(map);
            } else {
                map.put("code", 500);
                map.put("msg", "删除失败");
                return JSON.toJSONString(map);
            }
        }

    }


    @PostMapping("/delete-albums")
    public String deleteAlbum(@RequestHeader("Authorization") String authorization, @RequestParam("class_id") String class_id) {
        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null || !"admin".equals(JWTToken.isAdmin(authorization))) {
            return JSON.toJSONString(erroToken);
        } else {



                EntityWrapper<Albums> albumsEntityWrapper = new EntityWrapper<>();
                albumsEntityWrapper.eq("class_id",class_id);
                List<Albums> albums = albumsMapper.selectList(albumsEntityWrapper);
                //先删除子表数据
                albumsMapper.delete(albumsEntityWrapper);
                for (Albums album : albums) {
                    //删除图片
                    boolean b = deleteServerFile(album.getPath());
                }
                //后删除父表数据
            Integer delete = albumsClassesMapper.deleteById(Integer.parseInt(class_id));
                map = new HashMap<String, Object>();
                if (delete >0) {
                    map.put("code", 200);
                    map.put("msg", "删除成功");
                    return JSON.toJSONString(map);
                } else {
                    map.put("code", 500);
                    map.put("msg", "删除失败");
                    return JSON.toJSONString(map);
                }


        }

    }


    @PostMapping("/upImgs")
    public String uploadImg( @RequestParam("files") MultipartFile[] files, @RequestParam("name") String name) {


            if (files.length>0) { //文件不是空文件
                try {

                    for (MultipartFile file : files) {
                        String fileName = file.getOriginalFilename().toString();//获取文件名
                        final SimpleDateFormat sDateFormate = new SimpleDateFormat("yyyyMMddHHmmss");  //设置时间格式
                        String nowTimeStr = sDateFormate.format(new Date()); // 当前时间
                        fileName = fileName.substring(0, fileName.indexOf(".")) + nowTimeStr + fileName.substring(fileName.lastIndexOf("."));
                        BufferedOutputStream out = new BufferedOutputStream(
                                new FileOutputStream(new File(filepath + fileName)));//保存图片到目录下,建立保存文件的输入流
                        out.write(file.getBytes());
                        out.flush();
                        out.close();
                        String filename = filepath + fileName;
                        Long fileSize = file.getSize();
                        System.out.println(file.getSize());
                        //新建相册分类
                        AlbumsClasses newAlbumsClasses = new AlbumsClasses();
                        newAlbumsClasses.setAlbumName(name);
                        albumsClassesMapper.insert(newAlbumsClasses);
                        //根据新建相册分类查询所在id
                        Integer id = albumsClassesMapper.selectOne(newAlbumsClasses).getId();
                        Albums albums = new Albums();
                        albums.setClassId(id);
                        albums.setPath(filename);
                        albumsMapper.insert(albums);
                    }

                    map = new HashMap<String, Object>();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    map.put("code", 500);
                    map.put("msg", "上传失败"+e.getMessage());
                    return JSON.toJSONString(map);
                    //return "上传失败," + e.getMessage();  //文件路径错误
                } catch (IOException e) {
                    e.printStackTrace();
                    map.put("code", 500);
                    map.put("msg", "上传失败"+e.getMessage());
                    return JSON.toJSONString(map);

                    //return "上传失败," + e.getMessage();  //文件IO错误
                }
                map.put("code", 200);
                map.put("msg", "上传图片成功");
                return JSON.toJSONString(map);
                //return new Reponse(true, "上传头像成功", user);//返回用户信息
            } else {
                map.put("code", 500);
                map.put("msg", "上传失败，因为文件是空的");
                return JSON.toJSONString(map);
                //return new Reponse(false, "上传失败，因为文件是空的");
            }

    }


    /**
     *
     * @param files
     * @param name
     * @param class_id
     * @return
     */
    @PostMapping("/updateImgs")
    public String updateImg( @RequestParam("files") MultipartFile[] files, @RequestParam("name") String name,@RequestParam("class_id") String class_id,
    @RequestParam("remove_img") String removeImg[]
    ) {

        //首先删除本地图片，还有数据库图片路径path
        if(removeImg.length>0){
            for (String path : removeImg) {
                boolean isDelete = deleteServerFile(path);

            }

            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(removeImg));
            albumsMapper.deleteBatchIds(arrayList);
            //albumsMapper.deleteByArrayForPhoto(removeImg);
        }


        if (files.length>0) { //文件不是空文件
            try {


                //进行添加新的图片
                EntityWrapper<Albums> albumsEntityWrapper = new EntityWrapper<>();
                albumsEntityWrapper.eq("class_id",class_id);

                //重新添加照片
                for (MultipartFile file : files) {
                    String fileName = file.getOriginalFilename().toString();//获取文件名
                    final SimpleDateFormat sDateFormate = new SimpleDateFormat("yyyyMMddHHmmss");  //设置时间格式
                    String nowTimeStr = sDateFormate.format(new Date()); // 当前时间
                    fileName = fileName.substring(0, fileName.indexOf(".")) + nowTimeStr + fileName.substring(fileName.lastIndexOf("."));
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(new File(filepath + fileName)));//保存图片到目录下,建立保存文件的输入流
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                    String filename = filepath + fileName;
                    Long fileSize = file.getSize();
                    System.out.println(file.getSize());

                    Albums album = new Albums();
                    album.setClassId(Integer.parseInt(class_id));
                    album.setPath(filename);
                    albumsMapper.insert(album);
                }

                map = new HashMap<String, Object>();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                map.put("code", 500);
                map.put("msg", "上传失败"+e.getMessage());
                return JSON.toJSONString(map);
                //return "上传失败," + e.getMessage();  //文件路径错误
            } catch (IOException e) {
                e.printStackTrace();
                map.put("code", 500);
                map.put("msg", "上传失败"+e.getMessage());
                return JSON.toJSONString(map);

                //return "上传失败," + e.getMessage();  //文件IO错误
            }
            map.put("code", 200);
            map.put("msg", "上传图片成功");
            return JSON.toJSONString(map);
            //return new Reponse(true, "上传头像成功", user);//返回用户信息
        } else {
            map.put("code", 500);
            map.put("msg", "上传失败，因为文件是空的");
            return JSON.toJSONString(map);
            //return new Reponse(false, "上传失败，因为文件是空的");
        }

    }

    @PostMapping("/get-albums-cover")
    public String getAlbumCover(@RequestHeader("Authorization") String authorization,@RequestBody HashMap<String,String> requestMap){


        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null) {
            return JSON.toJSONString(erroToken);
        } else {
            List<AlbumsClasses> albumsClasses = albumsClassesMapper.selectList(new EntityWrapper<AlbumsClasses>());

            //查找所有用户数count
            int albumsClassesCount = albumsClassesMapper.selectCount(new EntityWrapper<AlbumsClasses>());
            map = new HashMap<String, Object>();
            //分页查找用户
            EntityWrapper<Albums> queryWrapper = new EntityWrapper<>();
            queryWrapper.orderBy("id", true);
            int currentPage = Integer.parseInt(requestMap.get("currentPage"));
            int pageSize = Integer.parseInt(requestMap.get("pageSize"));
//            // 查询第currentPage页，每页返回pageSize条
//            List<Albums> albums = albumsMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
            Page<AlbumsVO> page = new Page<>(currentPage,pageSize);
            List<AlbumsVO> cover = albumsMapper.getCover(page);

            map = new HashMap<String, Object>();

            map.put("code", 200);
            map.put("msg", "查询成功");
            map.put("cover_list", cover);
            map.put("total", albumsClassesCount);
            map.put("currentPage", currentPage);
            map.put("pageSize", pageSize);

            return JSON.toJSONString(map);
        }
    }

    @GetMapping("/get-albums")
    public String getAlbums(@RequestHeader("Authorization") String authorization,@RequestParam("class_id") String class_id){


        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null) {
            return JSON.toJSONString(erroToken);
        } else {
           Map map = new HashMap<String, Integer>();
           map.put("class_id", Integer.parseInt(class_id));
            List<Albums> album_list = albumsMapper.selectByMap(map);
            List list = new ArrayList<String>();
            for (Albums albums : album_list) {
                String path = albums.getPath();
                list.add(path);
            }
            String name = albumsClassesMapper.selectById(Integer.parseInt(class_id)).getAlbumName();


            map = new HashMap<String, Object>();

            map.put("code", 200);
            map.put("msg", "查询成功");
            map.put("album_list", list);
            map.put("album_name", name);

            return JSON.toJSONString(map);
        }
    }

    @GetMapping("/sys/get-albums")
    public String getAlbumsForSYS(@RequestHeader("Authorization") String authorization,@RequestParam("class_id") String class_id){


        Map<String, Object> erroToken = JWTVerify.verifyToken(authorization);
        if (erroToken != null) {
            return JSON.toJSONString(erroToken);
        } else {
            Map map = new HashMap<String, Integer>();
            map.put("class_id", Integer.parseInt(class_id));
            List<Albums> album_list = albumsMapper.selectByMap(map);
            String name = albumsClassesMapper.selectById(Integer.parseInt(class_id)).getAlbumName();

            map = new HashMap<String, Object>();

            map.put("code", 200);
            map.put("msg", "查询成功");
            map.put("album_list", album_list);
            map.put("album_name", name);

            return JSON.toJSONString(map);
        }
    }




    /**
     * 删除服务上的文件
     * @param filePath 路径
     * @param
     * @return
     */
    public static boolean deleteServerFile(String filePath){
        boolean delete_flag = false;
        File file = new File(filePath);
        if (file.exists() && file.isFile() && file.delete()){
            delete_flag = true;
        }
        else{
            delete_flag = false;
        }
        return delete_flag;
    }

}

