package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if (item.getId() == null){
            em.persist(item); //등록된 적 없는 신규 item
        }else{
            em.merge(item); //이미 DB에 등록된 item
            //Item mergeItem = em.merge(item); //mergeItem: 영속성 컨텍스트, item: 준영속성 컨텍스트
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
        .getResultList();
    }
}
