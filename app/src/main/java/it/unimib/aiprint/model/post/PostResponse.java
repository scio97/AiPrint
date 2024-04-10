package it.unimib.aiprint.model.post;

import java.util.List;

public class PostResponse {
    private List<Post> postList;
    private Post post;

    public PostResponse(Post post){
        this.setPost(post);
    }

    public PostResponse(List<Post> newsList) {
        this.postList = newsList;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
