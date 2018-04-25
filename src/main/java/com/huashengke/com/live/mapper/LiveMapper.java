package com.huashengke.com.live.mapper;

import com.huashengke.com.live.body.LiveRoom;
import com.huashengke.com.live.body.LiveChangeBody;
import com.huashengke.com.live.body.Live;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Mapper
@Component
public interface LiveMapper {
    @Select("select * from live_info where live_id = #{live_room_id}")
    LiveRoom getLive(@Param("live_room_id") String liveRoomId);

    @Insert("insert into tbl_vw_live" +
            "  (id, app, title, cover, stream, 'status', chat_room_id, live_time, user_id," +
            "  user_intro, live_notice, chapter_id, online_number, definition, is_push, is_record_open, is_record)" +
            "  VALUES(#{id}, #{app}, #{title}, #{cover}, #{stream}, #{status}, #{chatRoomId}, #{startTime}, #{userId}, #{userIntro}," +
            "  #{liveNotice}, #{chapterId}, #{onLineNumber}, #{definition}, #{isPush}, #{isRecordOpen}, #{isRecord})")
    void addLiveRoom(@Param("id") String liveRoomId, @Param("title")String title, @Param("cover")String cover,
                    @Param("status")String status, @Param("chatRoomId")String chatRoomId,
                     @Param("userId")String userId, @Param("userIntro")String userIntro, @Param("liveNotice")String liveNotice,
                     @Param("definition")String definition);

    @Insert("insert into live_stream(stream , description, live_id, is_push, is_forbid, is_show, is_main, is_forbid)" +
            "values(#{liveStreamId},#{appName},#{streamName},#{description},#{isPush},#{isForbid},#{isShow},#{isMain})")
    void addLiveStream(@Param("streamName")String streamName, @Param("description")String description,
                       @Param("liveId")String liveId, @Param("isPush")int isPush, @Param("isForbid")int isForbid,
                       @Param("isShow")int isShow, @Param("isMain")int isMain);


    @Insert("insert into tbl_vw_live_stream (stream, description, live_id) VALUES (#{stream}, #{description}, #{liveId})")
    void addNewStream(@Param("stream") String stream, @Param("description") String description, @Param("liveId") String liveId);

    @Update("update tbl_vw_live_stream set description= #{description} where stream=#{stream} and live_id=#{liveId}")
    void changeStreamDescription(@Param("stream") String stream, @Param("description") String description, @Param("liveId") String liveId);

    @Update("update tbl_vw_live_stream set is_forbid= #{isForbid} where stream=#{stream} and live_id=#{liveId}")
    void changeStreamForbid(@Param("stream") String stream, @Param("liveId") String liveId, @Param("isForbid") int isForbid);

    @Update("update tbl_vw_live_stream set is_show= #{isShow} where stream=#{stream} and live_id=#{liveId}")
    void changeStreamShow(@Param("stream") String stream, @Param("liveId") String liveId, @Param("isShow") int isShow);

    @Update("update tbl_vw_live_stream set is_push= #{isPush} where stream=#{stream} and live_id=#{liveId}")
    void changeStreamPush(@Param("stream") String stream, @Param("liveId") String liveId, @Param("isPush") int isPush);

    @Update("update tbl_vw_new_live set is_push=#{isPush} where id=#{liveId}")
    void changeLivePush(@Param("liveId") String liveId, @Param("isPush") int isPush);

    @Update("update tbl_vw_live_stream set is_main=#{isMain} where stream=#{stream} and live_id=#{liveId}")
    void changeMainStream(@Param("stream") String stream, @Param("liveId") String liveId, @Param("isMain") int isMain);

    @Update("update tbl_vw_new_live set stream=#{stream} where id=#{liveId}")
    void changeLiveStream(@Param("stream") String stream, @Param("liveId") String liveId);

    @Update("update  set title=#{title},web_cover=#{webCover},live_notice=#{liveNotice},user_intro = #{userIntro}" +
            ",start_time=#{startTime},where id = #{liveId}")
    void changeLive(LiveChangeBody live);

    @Update("update tbl_vw_new_live set definition=#{definition} where id=#{liveId}")
    void changeDefinition(@Param("liveId") String liveId, @Param("definition") String definition);


    @Insert("INSERT INTO tbl_vw_live_record (live_id, start_time, end_time, duration, uri) " +
            "VALUES (#{liveId},#{startTime},#{endTime},#{duration},#{uri})")
    void addLiveRecord(@Param("liveId") String liveId, @Param("startTime") Date startTime,
                       @Param("endTime") Date endTime, @Param("duration") long duration, @Param("uri") String uri);

    @Update("update tbl_vw_live_chapter set max_online_number=?  where id=?")
    void changeMaxOnlineNumber(@Param("chapterId") String chapterId, @Param("maxOnlineNumber") int maxOnlineNumber);

    @Update("update tbl_vw_live_room set status = #{status}")
    void changeLiveRoomStatus(@Param("liveRoom") LiveRoom liveRoom, @Param("status") String status);

    @Update("update live_stream set is_push=#{liveStatus} where id=#{liveStreamId}")
    void changeLiveStatus(@Param("liveStreamId") String liveStreamId, @Param("liveStatus") String liveStatus);

    @Update("update tbl_vw_new_live set is_record_open = #{isRecordOpen} where id=#{liveId}")
    void changeRecordStatus(@Param("liveId") String liveId, @Param("isRecordOpen") int isRecordOpen);

    @Update("update tbl_vw_new_live set is_record = #{isRecord} where id=#{liveId}")
    void changeAliRecordStatus(@Param("liveId") String liveId, @Param("isRecord") int isRecord);

    @Update("update tbl_vw_live_chapter set title=#{title}, content=#{content}, start_time=#{startTime}, finish_time=#{finishTime}" +
            "where id=#{id}")
    void changeLiveChapter(Live live);

    @Delete("delete from tbl_vw_live_chapter where id = #{chapterId}")
    void deleteLiveChapter(@Param("chapterId") String chapterId);

    @Update("update tbl_vw_live_chapter set video_id=#{videoId}, have_video=1  where id=#{chapterId}")
    void changeLiveChapterVideo(@Param("videoId") String videoId, @Param("chapterId") String chapterId);

    @Insert("insert into tbl_vw_live_chapter (id,live_id, title,  content, start_time, finish_time,  is_free, apply_require)" +
            " VALUES (#{id},#{liveId},#{title},#{content},#{startTime},#{finishTime})")
    void addLiveChapter(Live live);

    @Update("update tbl_vw_new_live set next_chapter_id=#{nextChapterId} where id = #{liveId}")
    void changeNextChapterId(@Param("liveId") String liveId, @Param("nextChapterId") String nextChapterId);

}
