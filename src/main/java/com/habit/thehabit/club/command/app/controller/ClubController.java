package com.habit.thehabit.club.command.app.controller;

import com.habit.thehabit.club.command.app.dto.ClubDTO;
import com.habit.thehabit.club.command.app.dto.CreateClubDTO;
import com.habit.thehabit.club.command.app.dto.JoinClubDTO;
import com.habit.thehabit.club.command.app.dto.WithdrawDTO;
import com.habit.thehabit.club.command.app.exception.DuplicationException;
import com.habit.thehabit.club.command.app.exception.OverstaffedException;
import com.habit.thehabit.club.command.app.service.ClubService;
import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.util.UploadToS3;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.IOException;


@Tag(name = "clubs",description = "모임API")
@RestController
@RequestMapping("/v1/clubs")
@Slf4j
public class ClubController {

    private final ClubService clubService;

    private final UploadToS3 uploadToS3;
    @Autowired
    public ClubController(ClubService clubService, UploadToS3 uploadToS3) {
        this.clubService = clubService;
        this.uploadToS3 = uploadToS3;
    }

    @Operation(summary = "클럽생성API", description = "클럽을 생성하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "클럽 생성 성공", content = @Content(schema = @Schema(implementation = CreateClubDTO.class)))})
    @Parameters({
            @Parameter(name = "clubName", description = "모임명", example = "독서모임"),
            @Parameter(name = "bookName", description = "대상도서명", example = "어린왕자"),
            @Parameter(name = "clubIntro", description = "모임소개", example = "저희 독서모임은 ..."),
            @Parameter(name = "recruitStartDate", description = "모집시작일", example = "2022-11-01T12:00"),
            @Parameter(name = "recruitEndDate", description = "모집종료일", example = "2022-11-14T22:00"),
            @Parameter(name = "startDate", description = "모임시작일", example = "2022-11-15T00:00"),
            @Parameter(name = "endDate", description = "모임종료일", example = "2022-11-20T14:00"),
            @Parameter(name = "numberOfMember", description = "최대 참가 인원", example = "8"),
            @Parameter(name = "scheduleDTOList", description = "회의 일정", example = "[\n" +
                    "        {\"dayOfWeek\" : \"일\", \"startTime\" : \"21:05\", \"endTime\" : \"22:05\"},\n" +
                    "        {\"dayOfWeek\" : \"수\", \"startTime\" : \"15:00\", \"endTime\" : \"16:00\"},\n" +
                    "        {\"dayOfWeek\" : \"금\", \"startTime\" : \"17:00\", \"endTime\" : \"18:00\"}\n" +
                    "]")
            })

    /*club 생성*/
    /*2022-11-22수정*/
//    @PostMapping("")
//    public ResponseEntity<ResponseDTO> createClub(@RequestBody CreateClubDTO request) throws IOException {
//        System.out.println("클럽 개설 확인");
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "생성요청 성공", clubService.createClubs(request)));
//    }
//    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<ResponseDTO> createClub( @RequestPart CreateClubDTO request, @RequestPart MultipartFile imgFile ) throws IOException {
//        System.out.println("클럽 개설 확인");
//        String imageUri = uploadToS3.upload(imgFile,"club");
//        request.setImageUri(imageUri);
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "생성요청 성공", clubService.createClubs(request)));
//    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDTO> createClub(CreateClubDTO request, @RequestPart  MultipartFile imgFile) throws IOException {
        System.out.println("클럽 개설 확인");
        if(imgFile != null){
            String imageUri = uploadToS3.upload(imgFile,"club");
            request.setImageUri(imageUri);
//            request.setImgFile(null);
        }
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "생성요청 성공", clubService.createClubs(request)));
    }


//    @PostMapping(value = "")
//    public ResponseEntity<ResponseDTO> createClub(@RequestBody CreateClubDTO request) throws IOException {
//        System.out.println("클럽 개설 확인");
//
//        /**/
//        /* 방이미지 바이트배열 꺼내기 */
//
//        String imageUri = uploadToS3.upload(request.getImgFile(),"club");
//        request.setImageUri(imageUri);
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "생성요청 성공", clubService.createClubs(request)));
//    }

    @Operation(summary = "모임조회", description = "모임 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "클럽 생성 성공", content = @Content(mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = ClubDTO.class))))})
    /* 개설된 club목록 조회 */
    @GetMapping("")
    public ResponseEntity<ResponseDTO> getClubs(@RequestParam(value = "option", defaultValue = "-1")int option ,@RequestParam(value = "clubId", defaultValue = "-1")int clubId){
        if(clubId > 0){
            System.out.println("클럽상세조회");
            System.out.println("clubID :" + clubId);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "클럽 상세 조회 성공",clubService.getClubDetail(clubId)));
        }

        if(option == 1){
            //option이 1인 경우 참가한 클럽 조회
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "참여중인 클럽 조회성공",clubService.getMyClubs()));
        }
        if(option == 2){
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "모집중인 클럽 조회성공", clubService.getRecruitingClubs()));

        }
        System.out.println("========================= 클럽 조회 요청 컨트롤러 ===========================");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "모든 클럽 조회성공", clubService.findAllClubs()));
    }

    @Operation(summary = "모임 참가 신청", description = "모임 참가 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임 참가 성공", content = @Content(schema = @Schema(implementation = ClubDTO.class)))})
    @Parameters({
            @Parameter(name = "clubId", description = "clubId", example = "독서모임 ID")
    })
    /* club 참가 신청*/
    @PutMapping("")
    public ResponseEntity<ResponseDTO> joinClub(@RequestBody JoinClubDTO joinClubDTO){
        System.out.println("joinClub 요청확인");
        int clubId = joinClubDTO.getClubId();
        try {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "신청 성공", clubService.joinClub(clubId)));
        } catch (DuplicationException e){
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "이미 신청된 모임입니다.", null));
        } catch (OverstaffedException e){
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "모집인원을 초과하여 신청이 불가능합니다.", null));
        }
    }

    @Operation(summary = "모임 탈퇴", description = "모임 탈퇴 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임 참가 성공", content = @Content(schema = @Schema(implementation = ClubDTO.class)))})
    @Parameters({
            @Parameter(name = "clubId", description = "clubId", example = "독서모임 ID")
    })
    /* club 탈퇴 */
    @PatchMapping("")
    public ResponseEntity<ResponseDTO> withdrawClub(@RequestBody WithdrawDTO withdrawDTO ){

        System.out.println("컨트롤러 클럽 탈퇴 요청 확인");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "클럽 탈퇴 성공", clubService.withdrawClub(withdrawDTO)));
    }
}
