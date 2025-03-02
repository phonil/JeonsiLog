package depth.jeonsilog.domain.reply.presentation;

import depth.jeonsilog.domain.reply.application.ReplyService;
import depth.jeonsilog.domain.reply.dto.ReplyRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/replies")
public class ReplyController implements ReplyApi {

    private final ReplyService replyService;

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<?> findReplyList(
            @RequestParam(value = "page") Integer page,
            @PathVariable(value = "reviewId") Long reviewId
    ) {
        return replyService.findReplyList(page, reviewId);
    }

    @PostMapping
    public ResponseEntity<?> createReply(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ReplyRequestDto.CreateReplyReq createReplyReq
            ) throws IOException {
        return replyService.createReply(userPrincipal, createReplyReq);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "replyId") Long replyId
    ) {
        return replyService.deleteReply(userPrincipal, replyId);
    }

    @GetMapping("/{replyId}")
    public ResponseEntity<?> existReply(
            @PathVariable(value = "replyId") Long replyId
    ) {
        return replyService.existReply(replyId);
    }
}
