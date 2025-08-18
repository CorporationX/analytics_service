package faang.school.analytics.integration.post.service;

import faang.school.analytics.integration.post.dto.PostResponseDto;

public interface PostServiceClient {
    PostResponseDto getPost(long id);
}
