package com.bzh.dytt.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bzh.dytt.data.VideoDetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VideoDetailPageParser {


    private InternalVideoDetailParser mVideoDetailParser;

    @Inject
    public VideoDetailPageParser() {
        mVideoDetailParser = new InternalVideoDetailParser();
    }

    public VideoDetail parseVideoDetail(String html) {
        return mVideoDetailParser.parse(html);
    }


    private static class InternalVideoDetailParser {

        public VideoDetail parse(String s) {
            VideoDetail videoDetail = new VideoDetail();
            Document document = Jsoup.parse(s);
            String html = document.select("div.co_content8").select("ul").toString();
            html = html.substring(0, html.indexOf("</table>"));

            String publishTime = getPublishTime(html);
            String title = document.select("div.co_area2").select("div.title_all").select("font").first().text();
            String coverUrl = document.select("div.co_content8").select("ul").select("img").first().attr("src");
            String previewImage = document.select("div.co_content8").select("ul").select("img").last().attr("src");
            ArrayList<String> downloadUrls = getDownloadUrls(document);
            ArrayList<String> downloadNames = getDownloadNames(document);

            // "◎译　　名.*<br>"
            String patterRegular = "◎.*<br>";
            String splitRegular = "◎";
            fillMovieDetail(patterRegular, splitRegular, videoDetail, html);

            // [剧　名]:
            patterRegular = "\\[.*<br>";
            splitRegular = "\\[";
            fillMovieDetail(patterRegular, splitRegular, videoDetail, html);

            // 【译 　名】： 黑吃黑
            patterRegular = "【.*<br>";
            splitRegular = "【";
            fillMovieDetail(patterRegular, splitRegular, videoDetail, html);


            videoDetail.setCoverUrl(coverUrl);
            videoDetail.setPublishTime(publishTime);
//            entity.setName(title);
//            entity.setPublishTime(publishTime);
//            entity.setCoverUrl(coverUrl);
//            entity.setPreviewImage(previewImage);
//            entity.setDownloadUrls(downloadUrls);
//            entity.setDownloadNames(downloadNames);

//            videoDetail.setName(title);
            return videoDetail;
        }

        private ArrayList<String> getDownloadNames(Document document) {
            ArrayList<String> strings = new ArrayList<>();
            Elements elements = document.select("div.co_content8").select("ul").select("a");

            for (Element e : elements) {
                String href = e.attr("href");
                if (href.startsWith("ftp") || (href.startsWith("http") && !href.contains("www.ygdy8.net") && !href
                        .contains("www.dytt8.net") && !href.contains("www.dygod.cn"))) {
                    href = href.substring(href.indexOf("]") + 1, href.length()).replaceAll(".rmvb", "").replaceAll("" +
                            ".mkv", "").replaceAll(".mp4", "");
                    strings.add(href);
                }
            }
            return strings;
        }

        private ArrayList<String> getDownloadUrls(Document document) {
            ArrayList<String> strings = new ArrayList<>();
            Elements elements = document.select("div.co_content8").select("ul").select("a");

            for (Element e : elements) {
                String href = e.attr("href");
                if (href.startsWith("ftp") || (href.startsWith("http") && !href.contains("www.ygdy8.net"))) {
                    strings.add(href);
                }
            }
            return strings;
        }

        @NonNull
        private String getPublishTime(String html) {
            String publishTime;
            Pattern pattern = Pattern.compile("发布时间：.*&");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                publishTime = matcher.group();
                publishTime = publishTime.substring(publishTime.indexOf("：") + 1, publishTime.length() - 1);
            } else {
                publishTime = "";
            }
            return rejectHtmlSpaceCharacters(publishTime);
        }

        private void fillMovieDetail(String regular, String splitRegular, VideoDetail entity, String html) {
            Matcher matcher = Pattern.compile(regular).matcher(html);
            if (matcher.find()) {
                String result = rejectHtmlSpaceCharacters(matcher.group());
                String[] split = result.split(splitRegular);
                for (String aSplit : split) {
                    String info = rejectSpecialCharacter(aSplit);
                    if (info.startsWith(Const.NAME)) {
                        // 片名
                        info = info.substring(info.indexOf(Const.NAME) + Const.NAME.length());
                        entity.setName(info);
                    } else if (info.startsWith(Const.SOURCENAME)) {
                        // 原名
                        info = info.substring(info.indexOf(Const.SOURCENAME) + Const.SOURCENAME.length());
                        entity.setName(info);
                    } else if (info.startsWith(Const.TRANSLATIONNAME)) {
                        // 译名
                        info = info.substring(info.indexOf(Const.TRANSLATIONNAME) + Const.TRANSLATIONNAME.length());
//                        entity.setTranslationName(info);
                    } else if (info.startsWith(Const.YEARS)) {
                        // 年代
                        info = info.substring(info.indexOf(Const.YEARS) + Const.YEARS.length());
                        entity.setYears(info);
                    } else if (info.startsWith(Const.COUNTRY)) {
                        // 国家
                        info = info.substring(info.indexOf(Const.COUNTRY) + Const.COUNTRY.length());
                        entity.setCountry(info);
                    } else if (info.startsWith(Const.AREA)) {
                        // 地区
                        info = info.substring(info.indexOf(Const.AREA) + Const.AREA.length());
                        entity.setCountry(info);
                    } else if (info.startsWith(Const.CATEGORY)) {
                        // 类别
                        info = info.substring(info.indexOf(Const.CATEGORY) + Const.CATEGORY.length());
                        entity.setType(info);
                    } else if (info.startsWith(Const.LANGUAGE)) {
                        // 语言
                        info = info.substring(info.indexOf(Const.LANGUAGE) + Const.LANGUAGE.length());
                    } else if (info.startsWith(Const.SUBTITLE)) {
                        // 字幕
                        info = info.substring(info.indexOf(Const.SUBTITLE) + Const.SUBTITLE.length());
                    } else if (info.startsWith(Const.FILEFORMAT)) {
                        // 文件格式
                        info = info.substring(info.indexOf(Const.FILEFORMAT) + Const.FILEFORMAT.length());
                    } else if (info.toLowerCase().startsWith(Const.IMDB_GRADE.toLowerCase())) {
                        // IMDB评分
                        info = info.substring(info.toLowerCase().indexOf(Const.IMDB_GRADE.toLowerCase()) + Const
                                .IMDB_GRADE.length());
                        entity.setIMDBGrade(getGrade(info));
                        entity.setIMDBGradeUsers(getGradeUsers(info));
                    } else if (info.toLowerCase().startsWith(Const.DOUBAN_GRADE.toLowerCase())) {
                        // 豆瓣评分
                        info = info.substring(info.toLowerCase().indexOf(Const.DOUBAN_GRADE.toLowerCase()) + Const
                                .DOUBAN_GRADE.length());
                        entity.setDoubanGrade(getGrade(info));
                        entity.setDoubanGradeUsers(getGradeUsers(info));
                    } else if (info.startsWith(Const.VIDEOSIZE)) {
                        // 视频尺寸
                        info = info.substring(info.indexOf(Const.VIDEOSIZE) + Const.VIDEOSIZE.length());
                    } else if (info.startsWith(Const.FILESIZE)) {
                        // 文件大小
                        info = info.substring(info.indexOf(Const.FILESIZE) + Const.FILESIZE.length());
                        entity.setFileSize(info);
                    } else if (info.startsWith(Const.DURATION)) {
                        // 片长
                        info = info.substring(info.indexOf(Const.DURATION) + Const.DURATION.length());
                        entity.setDuration(info);
                    } else if (info.startsWith(Const.DIRECTOR)) {
                        // 导演
                        info = info.substring(info.indexOf(Const.DIRECTOR) + Const.DIRECTOR.length());
                        entity.setDirector(new ArrayList<String>());
                        if (info.contains("•")) {
                            String[] strings = info.split("•");
                            entity.getDirector().addAll(Arrays.asList(strings));
                        } else {
                            entity.getDirector().add(info);
                        }
                    } else if (info.startsWith(Const.LEADING_ROLE)) {
                        // 主演
                        info = info.substring(info.indexOf(Const.LEADING_ROLE) + Const.LEADING_ROLE.length());
                        entity.setLeadingRole(new ArrayList<String>());
                        if (info.startsWith("<br>")) {
                            String[] leadingplayers = info.split("<br>");
                            entity.getLeadingRole().addAll(Arrays.asList(leadingplayers));
                        } else {
                            entity.getLeadingRole().add(info);
                        }
                    } else if (info.startsWith(Const.DESCRIPTION)) {
                        // 描述
                        info = info.substring(info.indexOf(Const.DESCRIPTION) + Const.DESCRIPTION.length());
                        entity.setDescription(info);
                    } else if (info.startsWith(Const.SHOWTIME)) {
                        // 上映日期
                        info = info.substring(info.indexOf(Const.SHOWTIME) + Const.SHOWTIME.length());
                        entity.setShowTime(info);
                    }
//                    else if (info.startsWith(Const.EPISODENUMBER)) {
//                        // 集数
//                        info = info.substring(info.indexOf(Const.EPISODENUMBER) + Const.EPISODENUMBER.length());
//                        entity.setEpisodeNumber(info);
//                    }
//                    // 日韩电视剧
//                    else if (info.startsWith(Const.PLAYNAME)) {
//                        // 剧名
//                        info = info.substring(info.indexOf(Const.PLAYNAME) + Const.PLAYNAME.length());
//                        entity.setName(info);
//                    } else if (info.startsWith(Const.SOURCE)) {
//                        // 来源
//                        info = info.substring(info.indexOf(Const.SOURCE) + Const.SOURCE.length());
//                        entity.setSource(info);
//                    } else if (info.startsWith(Const.TYPE)) {
//                        // 类型
//                        info = info.substring(info.indexOf(Const.TYPE) + Const.TYPE.length());
//                        entity.setType(info);
//                    } else if (info.startsWith(Const.PREMIERE)) {
//                        // 首播
//                        info = info.substring(info.indexOf(Const.PREMIERE) + Const.PREMIERE.length());
//                        entity.setPlaytime(info);
//                    } else if (info.startsWith(Const.PREMIERE_TIME)) {
//                        // 首播时间
//                        info = info.substring(info.indexOf(Const.PREMIERE_TIME) + Const.PREMIERE_TIME.length());
//                        entity.setPlaytime(info);
//                    } else if (info.startsWith(Const.TIME)) {
//                        // 时间
//                        info = info.substring(info.indexOf(Const.TIME) + Const.TIME.length());
//                        entity.setPlaytime(info);
//                    } else if (info.startsWith(Const.JIE_DANG)) {
//                        // 接档
//                        info = info.substring(info.indexOf(Const.JIE_DANG) + Const.JIE_DANG.length());
//                        entity.setJieDang(info);
//                    } else if (info.startsWith(Const.SCREENWRITER)) {
//                        // 编剧
//                        info = info.substring(info.indexOf(Const.SCREENWRITER) + Const.SCREENWRITER.length());
//                        if (entity.getScreenWriters() == null) {
//                            entity.setScreenWriters(new ArrayList<String>());
//                        }
//                        if (info.contains("•")) {
//                            String[] strings = info.split("•");
//                            entity.getScreenWriters().addAll(Arrays.asList(strings));
//                        } else {
//                            entity.getScreenWriters().add(info);
//                        }
//                    }
//                    // 欧美电视剧
//                    else if (info.startsWith(Const.TVSTATION)) {
//                        // 电视台
//                        info = info.substring(info.indexOf(Const.TVSTATION) + Const.TVSTATION.length());
//                        entity.setSource(info);
//                    } else if (info.startsWith(Const.TVSTATION_1)) {
//                        // 电视台
//                        info = info.substring(info.indexOf(Const.TVSTATION_1) + Const.TVSTATION_1.length());
//                        entity.setSource(info);
//                    } else if (info.startsWith(Const.PERFORMER)) {
//                        // 演员
//                        info = info.substring(info.indexOf(Const.PERFORMER) + Const.PERFORMER.length());
//                        if (entity.getLeadingPlayers() == null) {
//                            entity.setLeadingPlayers(new ArrayList<String>());
//                        }
//                        if (info.contains("•")) {
//                            String[] strings = info.split("•");
//                            entity.getLeadingPlayers().addAll(Arrays.asList(strings));
//                        } else {
//                            entity.getLeadingPlayers().add(info);
//                        }
//                    }
                }
            }
        }

        private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        private static final String regEx_space = "(\\s|　|&nbsp;)*|\t|\r|\n";//定义空格回车换行符
        private static final String regEx_regular = "(\\]|:|】|：)*";

        private String rejectHtmlSpaceCharacters(String str) {
            Pattern p_html = Pattern.compile(regEx_html);
            Matcher m_html = p_html.matcher(str);
            str = m_html.replaceAll(""); // 过滤html标签
            Pattern p_space = Pattern.compile(regEx_space);
            Matcher m_space = p_space.matcher(str);
            str = m_space.replaceAll(""); // 过滤空格回车标签
            return str.trim(); // 返回文本字符串
        }

        private String rejectSpecialCharacter(String str) {
            Pattern compile = Pattern.compile(regEx_regular);
            Matcher matcher = compile.matcher(str);
            if (matcher.find()) {
                return matcher.replaceAll("");
            }
            return str;
        }

        private String getGrade(String str){
            String result = "0";
            if(TextUtils.isEmpty(str)) {
                return result;
            }
            try {
                result= str.split("/")[0];
            } catch (Exception e) {
                return result;
            }
            return result;
        }

        private String getGradeUsers(String str){
            String result = "0";

            if(TextUtils.isEmpty(str)) {
                return "0";
            }
            try {
                result = str.split("from")[1].split("users")[0];

            } catch (Exception e) {
                return result;
            }
            return result;
        }



    }
}
