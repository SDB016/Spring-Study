package com.firstHomePage.myBoard.repository;

import com.firstHomePage.myBoard.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    /**
     *  ์ ์ฅ
     */
    public void save(Post post){
        em.persist(post);
    }

    /**
     * ์กฐํ
     */
    public Post findOne(Long postId) {
        return em.find(Post.class, postId);
    }

    public List<Post> findAll(int page, int size) {
        return em.createQuery("select p from Post p", Post.class)
                .setFirstResult((page-1)*size)
                .setMaxResults(size)
                .getResultList();
    }

    public void delete(Post post){
        em.remove(post);
    }

    public List<Post> findAllByKeyword(String keyword) {
        return em.createQuery(
                "select p from Post p where p.title like :keyword", Post.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    public List<Post> findAllByNickname(String nickname) {
        return em.createQuery(
                "select p from Post p " +
                        "where p.createdBy like :nickname", Post.class)
                .setParameter("nickname", "%" + nickname + "%")
                .getResultList();
    }
}
