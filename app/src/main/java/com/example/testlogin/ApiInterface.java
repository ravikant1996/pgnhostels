package com.example.testlogin;


import com.example.testlogin.Models.Room;
import com.example.testlogin.Models.RoomResponse;
import com.example.testlogin.Models.SignUpResponse;
import com.example.testlogin.Models.User;
import com.example.testlogin.Models.proImages;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("getAllPid.php")
    Call<SignUpResponse> getAll_pid();

    @FormUrlEncoded
    @POST("deactivate.php")
    Call<User> deactivate( @Field("status") String status);

    @GET("getStates.php")
    Call<SignUpResponse> getStates();

    @GET("getCity.php")
    Call<SignUpResponse> getCity(@Query("uid")Integer uid);

    @GET("getAllAdmin.php")
    Call<SignUpResponse> getAll_Admin();


    @GET("get_pid_All_Admin.php")
    Call<SignUpResponse> get_pid_All_Admin(@Query("pid")Integer pid);

    @GET("getAdminDetails.php")
    Call<User> getAdmindetails(@Query("aid")Integer aid);

    @GET("mainlogin.php")
    Call<User> login(@Query("email") String email, @Query("password") String password);

    @FormUrlEncoded
    @POST("addManager.php")
    Call<User> registerManager( @Field("name") String name,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("phone") String phone,
                                    @Field("aid") Integer aid,
                                    @Field("Status") Boolean Status);

    @GET("stafftype.php")
    Call<RoomResponse> getStafftype();

    @FormUrlEncoded
    @POST("addstaff.php")
    Call<User> registerStaff( @Field("name") String name,
                                @Field("email") String email,
                                @Field("address") String address,
                                @Field("phone") String phone,
                                @Field("aid") Integer aid,
                                @Field("stafftype") String  stafftype,
                                @Field("Status") Boolean Status,
                                @Field("joiningdate") String joiningdate);


    @FormUrlEncoded
    @POST("addnew_pg.php")
    Call<User> registernewPG(@Field("owner") String owner,
                                @Field("phone") String phone,
                                 @Field("Status") Boolean Status);

    @FormUrlEncoded
    @POST("addAdmin.php")
    Call<User> registerAdmin(@Field("name") String name,
                             @Field("email") String password,
                             @Field("password") String email,
                             @Field("owner") String owner,
                             @Field("location") String location,
                             @Field("phone") String phone,
                             @Field("Status") Boolean Status,
                             @Field("pid") Integer pid);
    @FormUrlEncoded
    @POST("admin_details.php")
    Call<User> registerAdminDetails( @Field("contact_person") String contact_person,
                                     @Field("phone") String phone,
                                     @Field("locatity") String locatity,
                                     @Field("city") String city,
                                     @Field("state") String state,
                                     @Field("description") String description,
                                     @Field("aid") Integer aid);

    @FormUrlEncoded
    @POST("addUser.php")
    Call<User> registerUser(
                             @Field("name") String name,
                             @Field("email") String password,
                             @Field("password") String email,
                             @Field("phone") String phone,
                             @Field("aid") Integer aid,
                             @Field("Status") Boolean Status);

    @FormUrlEncoded
    @POST("addRoom.php")
    Call<Room>Add_Room(@Field("room_no") String room_no,
                       @Field("no_of_bed") Integer no_of_bed,
                       @Field("aid") Integer aid);

    @FormUrlEncoded
    @POST("addBed.php")
    Call<Room>Add_bed(@Field("bed_no") String bed_no,
                       @Field("rid") Integer rid,
                      @Field("aid") Integer aid);



    @GET("get_room_details.php")
    Call<RoomResponse> getroomdetails(@Query("aid") Integer aid);


    @GET("roomdetailsforBedAllotment.php")
    Call<RoomResponse> roomdetailsforBedAllotment(@Query("aid") Integer aid);

    @GET("bedallotment.php")
    Call<RoomResponse> getbeddetails(@Query("aid") Integer aid,
                                     @Query("rid") Integer rid);

    @GET("tenentAllotment.php")
    Call<SignUpResponse> getTenentdetails(@Query("aid") Integer aid);

    @FormUrlEncoded
    @POST("assignBed.php")
    Call<Room> assignBed(@Field("bid") Integer bid,
                        @Field("uid") Integer uid,
                        @Field("aid") Integer aid,
                        @Field("Status") Boolean Status);




    @FormUrlEncoded
    @POST("room_count.php")
    Call<Room>room_count(@Field("rid") Integer rid,
                         @Field("aid") Integer aid);

    @FormUrlEncoded
    @POST("user_count.php")
    Call<User>user_count(@Field("aid") Integer aid);

    @FormUrlEncoded
    @POST("count_room_beds.php")
    Call<Room>room_bed_count(@Field("aid") Integer aid);

    @FormUrlEncoded
    @POST("updateUser.php")
    Call<ResponseBody> updateUser(@Field("id") Integer id,
                                  @Field("name") String password,
                                  @Field("image") String image);

    @FormUrlEncoded
    @POST("updateAdminPassword.php")
    Call<ResponseBody> updatePasswordUser(@Field("id") Integer id,
                                          @Field("password") String password);
    @FormUrlEncoded
    @POST("changePassword.php")
    Call<User> updatepassword(@Field("id") Integer id,
                              @Field("password") String password,
                              @Field("type") String type);

    @FormUrlEncoded
    @POST("uploadimage.php")
    Call<Room> uploadimage(@Field("images") String images);

    @GET("getAllTenent.php")
    Call<SignUpResponse> getUserDetails(@Query("aid") Integer aid);

    @GET("getAllStaffDetails.php")
    Call<SignUpResponse> getStaffList(@Query("aid")Integer aid);

    @POST("getPromotionImage.php")
    Call<proImages> getImage();

    @FormUrlEncoded
    @POST("clear.php")
    Call<ResponseBody> clear(@Field("id1") Integer id1,
                             @Field("id2") Integer id2,
                             @Field("id3") Integer id3,
                             @Field("aid") Integer aid,
                             @Field("type") String type);



    @GET("getManager.php")
    Call<User> getManager(@Query("aid")Integer aid);

    @GET("getMessageFromServer.php")
    Call<SignUpResponse> getAllmessage(@Query("id") Integer id,
                                       @Query("complaint_table") String complaint_table);

    @FormUrlEncoded
    @POST("createChatRoomAndSendMessage.php")
    Call<User>createChatRoomAndSendMessage(@Field("id") Integer id,
                                           @Field("category") String category,
                                           @Field("user_type") String user_type,
                                           @Field("message") String message,
                                           @Field("cid") Integer cid);
}
